package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;


class FutureTest {
    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }


    @Test
    void get() {
        // check pre condition
        assertFalse(future.isDone());
        //prepare
        future.resolve("");
        //act
        future.get();
        //assert
        assertTrue(future.isDone());
    }

    @Test
    void resolve() {
        // prepare
        String str = "someResult";
        // act
        future.resolve(str);
        // assert
        assertTrue(future.isDone());
        assertEquals(str, future.get());
    }

    @Test
    void isDone() {
        //prepare
        String str = "someResult";
        assertFalse(future.isDone()); // pre condition
        future.resolve(str);
        // act
        boolean result = future.isDone();
        //assert
        assertTrue(result);
    }

    @Test
    void testGet() {
        // check pre condition
        assertFalse(future.isDone());
        future.get(100, TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        //prepare
        future.resolve("foo");
        //act
        String result = future.get(100,TimeUnit.MILLISECONDS);
        //assert
        assertEquals(result,"foo");
    }
}