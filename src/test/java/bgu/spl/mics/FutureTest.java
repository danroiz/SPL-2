package bgu.spl.mics;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;


public class FutureTest {
    private Future<String> future;

    @Before
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet()
    {
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
    public void testResolve(){
        // prepare
        String str = "someResult";
        // act
        future.resolve(str);
        // assert
        assertTrue(future.isDone());
        assertEquals(str, future.get());
    }

    @Test
    public void testIsDone(){
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
    public void testGetWithTimeOut() throws InterruptedException
    {
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