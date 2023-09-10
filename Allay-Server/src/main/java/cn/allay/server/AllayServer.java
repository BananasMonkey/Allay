package cn.allay.server;

import cn.allay.api.client.Client;
import cn.allay.api.client.info.DeviceInfo;
import cn.allay.api.client.skin.Skin;
import cn.allay.api.network.NetworkServer;
import cn.allay.api.server.Server;
import cn.allay.api.server.ServerSettings;
import cn.allay.api.world.World;
import cn.allay.api.world.WorldPool;
import cn.allay.server.client.AllayClient;
import cn.allay.server.network.AllayNetworkServer;
import cn.allay.server.terminal.AllayTerminalConsole;
import cn.allay.server.world.AllayWorld;
import cn.allay.server.world.AllayWorldPool;
import cn.allay.server.world.generator.flat.FlatWorldGenerator;
import cn.allay.server.world.storage.nonpersistent.AllayNonPersistentWorldStorage;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Slf4j
public final class AllayServer implements Server {
    private final boolean DEBUG = false;
    private final Map<String, Client> clients = new ConcurrentHashMap<>();
    @Getter
    private final WorldPool worldPool = new AllayWorldPool();
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final Object2ObjectMap<UUID, PlayerListPacket.Entry> playerListEntryMap = new Object2ObjectOpenHashMap<>();
    //执行CPU密集型任务的线程池
    @Getter
    private final ForkJoinPool computeThreadPool = new ForkJoinPool(
            Runtime.getRuntime().availableProcessors() + 1,
            pool -> {
                ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                worker.setName("computation-thread-" + worker.getPoolIndex());
                return worker;
            },
            null, true);
    //执行IO密集型任务的线程池
    @Getter
    private final ExecutorService virtualThreadPool = Executors.newVirtualThreadPerTaskExecutor();
    @Getter
    private ServerSettings serverSettings;
    @Getter
    private NetworkServer networkServer;
    private Thread terminalConsoleThread;
    private AllayTerminalConsole terminalConsole;
    @Getter
    private long ticks = 0;

    @Override
    public void start() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration log4jConfig = ctx.getConfiguration();
        LoggerConfig loggerConfig = log4jConfig.getLoggerConfig(org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME);
        if (DEBUG && Level.TRACE.isLessSpecificThan(loggerConfig.getLevel())) {
            loggerConfig.setLevel(Level.TRACE);
            ctx.updateLoggers();
        }

        initTerminalConsole();
        this.serverSettings = readServerSettings();
        this.networkServer = initNetwork();
        log.info("Starting up network server...");
        this.networkServer.start();
        log.info("Network server started.");
        loadWorlds();
        loop();
    }

    private void loop() {
        GameLoop.builder()
                .loopCountPerSec(20)
                .onTick(loop -> {
                    if (isRunning.get())
                        onTick();
                    else loop.stop();
                })
                .onStop(() -> isRunning.set(false))
                .build()
                .startLoop();
    }

    private void initTerminalConsole() {
        terminalConsole = new AllayTerminalConsole(this);
        terminalConsoleThread = new AllayTerminalConsoleThread();
        terminalConsoleThread.start();
    }

    private void loadWorlds() {
        worldPool.setDefaultWorld(AllayWorld
                .builder()
                .setWorldGenerator(new FlatWorldGenerator())
                .setWorldStorage(new AllayNonPersistentWorldStorage())
                .build());
    }

    @Override
    public void shutdown() {
        isRunning.compareAndSet(true, false);
        virtualThreadPool.shutdownNow();
        computeThreadPool.shutdownNow();
        getWorldPool().getWorlds().values().forEach(World::close);
        System.exit(0);
    }

    private void onTick() {
        ticks++;
    }

    @Override
    public int getOnlineClientCount() {
        return clients.size();
    }

    @Override
    @UnmodifiableView
    public Map<String, Client> getOnlineClients() {
        return Collections.unmodifiableMap(clients);
    }

    private ServerSettings readServerSettings() {
        //TODO
        return ServerSettings
                .builder()
                .ip("0.0.0.0")
                .port(19132)
                .motd("Allay Server")
                .subMotd("Powered by Allay")
                .maxClientCount(20)
                .gameType(GameType.CREATIVE)
                .xboxAuth(false)
                .defaultTickingRadius(8)
                .defaultViewDistance(8)
                .enableNetworkEncryption(true)
                .build();
    }

    private NetworkServer initNetwork() {
        return new AllayNetworkServer(this);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void onClientConnect(BedrockServerSession session) {
        AllayClient.hold(session, this);
    }

    @Override
    public void onLoginFinish(Client client) {
        clients.put(client.getName(), client);
        networkServer.setPlayerCount(clients.size());
    }

    @Override
    public void onClientDisconnect(Client client) {
        clients.remove(client.getName());
        networkServer.setPlayerCount(clients.size());
    }

    @Override
    public void addToPlayerList(Client client) {
        addToPlayerList(
                client.getLoginData().getUuid(),
                client.getPlayerEntity().getUniqueId(),
                client.getName(),
                client.getLoginData().getDeviceInfo(),
                client.getLoginData().getXuid(),
                client.getLoginData().getSkin()
        );
    }

    @Override
    public void addToPlayerList(UUID uuid, long entityId,
                                String name, DeviceInfo deviceInfo,
                                String xuid, Skin skin) {
        var playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.Action.ADD);
        PlayerListPacket.Entry entry = new PlayerListPacket.Entry(uuid);
        entry.setEntityId(entityId);
        entry.setName(name);
        entry.setXuid(xuid);
        entry.setPlatformChatId(deviceInfo.getDeviceName());
        entry.setBuildPlatform(deviceInfo.getDevice().getId());
        entry.setSkin(skin.toNetwork());
        entry.setTrustedSkin(skin.isTrusted());
        playerListPacket.getEntries().add(entry);
        playerListEntryMap.put(uuid, entry);
        broadcastPacket(playerListPacket);
    }

    public void removeFromPlayerList(Client client) {
        var playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.Action.REMOVE);
        playerListPacket.getEntries().add(new PlayerListPacket.Entry(client.getLoginData().getUuid()));
        broadcastPacket(playerListPacket);
        playerListEntryMap.remove(client.getLoginData().getUuid());
    }

    public void removeFromPlayerList(UUID uuid) {
        var playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.Action.REMOVE);
        playerListPacket.getEntries().add(new PlayerListPacket.Entry(uuid));
        broadcastPacket(playerListPacket);
        playerListEntryMap.remove(uuid);
    }

    public Map<UUID, PlayerListPacket.Entry> getPlayerListEntryMap() {
        return this.playerListEntryMap;
    }

    @Override
    public void broadcastPacket(BedrockPacket packet) {
        for (var client : clients.values()) {
            client.sendPacket(packet);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    private class AllayTerminalConsoleThread extends Thread {
        public AllayTerminalConsoleThread() {
            super("Console Thread");
        }

        @Override
        public void run() {
            terminalConsole.start();
        }
    }
}
