package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.C3POMicroservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {
    private MessageBus bus;
    private MicroService receiver;

    @BeforeEach
    void setUp() {
        bus = MessageBusImpl.getInstance();
        receiver = new C3POMicroservice();
        bus.register(receiver); // check register work
    }

    @AfterEach
    void tearDown(){
        bus.unregister(receiver);
    }

    @Test
    void complete() {
        //prepare
        Event<Boolean> event = new AttackEvent();
        bus.subscribeEvent(AttackEvent.class, receiver);
        Future<Boolean> future = bus.sendEvent(event);
        //act
        bus.complete(event,true);
        //assert
        if (future != null)
            assertEquals(future.get(),true);
        else
            fail();
    }

    @Test
    void sendBroadcast() {
        //prepare
        Broadcast broadcast = new TerminateBroadcast();
        MicroService receiver2 = new C3POMicroservice();
        bus.register(receiver2);
        bus.subscribeBroadcast(TerminateBroadcast.class, receiver);
        bus.subscribeBroadcast(TerminateBroadcast.class,receiver2);
        Message receiver_msg = null;
        Message receiver2_msg = null;
        //act
        bus.sendBroadcast(broadcast);
        //assert
        try {
            receiver_msg = bus.awaitMessage(receiver); // also checks awaitMessage;
            receiver2_msg = bus.awaitMessage(receiver2);
        }
        catch (Exception e) {
            fail();
        }
        assertEquals(broadcast,receiver_msg);
        assertEquals(broadcast,receiver2_msg);
        //after
        bus.unregister(receiver2);
    }

    @Test
    void sendEvent() {
        //prepare
        Event<Boolean> event = new AttackEvent();
        bus.subscribeEvent(AttackEvent.class, receiver); // also checks subscribeEvent
        Message result = null;
        //act
        bus.sendEvent(event); // checks sendEvent
        //assert
        try {
            result = bus.awaitMessage(receiver); // also checks awaitMessage;
        }
        catch (Exception e){
            fail();
        }
        assertEquals(event,result);
    }
}
