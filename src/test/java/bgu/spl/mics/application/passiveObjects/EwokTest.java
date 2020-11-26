package bgu.spl.mics.application.passiveObjects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EwokTest {
    private Ewok ewok;
    final int DEFAULT_ID = 0;

    @Before
    public void setUp() throws Exception {
        ewok = new Ewok(DEFAULT_ID);
    }

    @Test
    public void acquire() {
        // check pre condition
        assertTrue(ewok.available);
        // act
        ewok.acquire();
        // assert
        assertFalse(ewok.available);
    }

    @Test
    public void release() {
        // prepare
        ewok.available = false;
        // act
        ewok.release();
        // assert
        assertTrue(ewok.available);
    }
}