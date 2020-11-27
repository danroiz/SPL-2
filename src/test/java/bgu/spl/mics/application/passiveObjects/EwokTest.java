package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EwokTest {
    private Ewok ewok;
    final int DEFAULT_ID = 0;

    @BeforeEach
    void setUp() { ewok = new Ewok(DEFAULT_ID); }

    @Test
    void acquire() {
        // check pre condition
        assertTrue(ewok.available);
        // act
        ewok.acquire();
        // assert
        assertFalse(ewok.available);
    }

    @Test
    void release() {
        // prepare
        ewok.available = false;
        // act
        ewok.release();
        // assert
        assertTrue(ewok.available);
    }
}