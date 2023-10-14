package cn.allay.api.client.data;

import cn.allay.api.entity.interfaces.player.EntityPlayer;
import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.AbilityLayer;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAbilitiesPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAdventureSettingsPacket;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Allay Project 2023/7/9
 *
 * @author JukeboxMC | daoge_cmd
 */
public class AdventureSettings {

    private final EntityPlayer player;
    private final Map<Type, Boolean> values = new EnumMap<>(Type.class);
    @Getter
    private float walkSpeed = 0.1f;
    @Getter
    private float flySpeed = 0.05f;

    public AdventureSettings(EntityPlayer player) {
        this.player = player;
    }

    public AdventureSettings set(Type type, boolean value) {
        this.values.put(type, value);
        return this;
    }

    public boolean get(Type type) {
        final Boolean value = this.values.get(type);
        return value == null ? type.getDefaultValue() : value;
    }

    public void setFlySpeed(float value) {
        this.flySpeed = value;
    }

    public void setWalkSpeed(float value) {
        this.walkSpeed = value;
    }

    public void update() {
        UpdateAbilitiesPacket updateAbilitiesPacket = new UpdateAbilitiesPacket();
        updateAbilitiesPacket.setUniqueEntityId(player.getUniqueId());
        updateAbilitiesPacket.setCommandPermission(player.isOp() ? CommandPermission.ADMIN : CommandPermission.ANY);
        updateAbilitiesPacket.setPlayerPermission(player.isOp() && player.getGameType() != GameType.SPECTATOR ? PlayerPermission.OPERATOR : PlayerPermission.MEMBER);

        AbilityLayer abilityLayer = new AbilityLayer();
        abilityLayer.setLayerType(AbilityLayer.Type.BASE);
        abilityLayer.getAbilitiesSet().addAll(Arrays.asList(Ability.values()));

        for (Type type : Type.values()) {
            if (type.getAbility() != null && this.get(type)) {
                abilityLayer.getAbilityValues().add(type.getAbility());
            }
        }

        abilityLayer.getAbilityValues().add(Ability.WALK_SPEED);
        abilityLayer.getAbilityValues().add(Ability.FLY_SPEED);

        if (player.getGameType() == GameType.CREATIVE) {
            abilityLayer.getAbilityValues().add(Ability.INSTABUILD);
        }

        if (player.isOp()) {
            abilityLayer.getAbilityValues().add(Ability.OPERATOR_COMMANDS);
        }

        abilityLayer.setWalkSpeed(this.walkSpeed);
        abilityLayer.setFlySpeed(this.flySpeed);

        updateAbilitiesPacket.getAbilityLayers().add(abilityLayer);

        UpdateAdventureSettingsPacket updateAdventureSettingsPacket = new UpdateAdventureSettingsPacket();
        updateAdventureSettingsPacket.setAutoJump(this.get(Type.AUTO_JUMP));
        updateAdventureSettingsPacket.setImmutableWorld(this.get(Type.WORLD_IMMUTABLE));
        updateAdventureSettingsPacket.setNoMvP(this.get(Type.NO_MVP));
        updateAdventureSettingsPacket.setNoPvM(this.get(Type.NO_PVM));
        updateAdventureSettingsPacket.setShowNameTags(this.get(Type.SHOW_NAME_TAGS));

        player.handleChunkPacket(updateAbilitiesPacket);
        player.handleChunkPacket(updateAdventureSettingsPacket);
    }

    @Getter
    public enum Type {
        WORLD_IMMUTABLE(false),
        NO_PVM(false),
        NO_MVP(Ability.INVULNERABLE, false),
        SHOW_NAME_TAGS(true),
        AUTO_JUMP(true),
        ALLOW_FLIGHT(Ability.MAY_FLY, false),
        NO_CLIP(Ability.NO_CLIP, false),
        WORLD_BUILDER(Ability.WORLD_BUILDER, true),
        FLYING(Ability.FLYING, false),
        MUTED(Ability.MUTED, false),
        MINE(Ability.MINE, true),
        DOORS_AND_SWITCHED(Ability.DOORS_AND_SWITCHES, true),
        OPEN_CONTAINERS(Ability.OPEN_CONTAINERS, true),
        ATTACK_PLAYERS(Ability.ATTACK_PLAYERS, true),
        ATTACK_MOBS(Ability.ATTACK_MOBS, true),
        OPERATOR(Ability.OPERATOR_COMMANDS, false),
        TELEPORT(Ability.TELEPORT, false),
        BUILD(Ability.BUILD, true);

        private final Ability ability;
        private final Boolean defaultValue;

        Type(Ability ability, Boolean defaultValue) {
            this.ability = ability;
            this.defaultValue = defaultValue;
        }

        Type(Boolean defaultValue) {
            this.defaultValue = defaultValue;
            this.ability = null;
        }
    }
}
