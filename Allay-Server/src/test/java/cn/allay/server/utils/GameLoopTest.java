package cn.allay.server.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author daoge_cmd <br>
 * @date 2023/4/15 <br>
 * Allay Project <br>
 */
class GameLoopTest {
    @Test
    void testLoop() {
        var counter = new AtomicInteger(0);
        GameLoop.builder()
                .loopCountPerSec(1000000)
                .onTick((loop -> {
                    if (counter.incrementAndGet() == 100)
                        loop.stop();
                }))
                .build()
                .startLoop();
        assertEquals(100, counter.get());
    }
}