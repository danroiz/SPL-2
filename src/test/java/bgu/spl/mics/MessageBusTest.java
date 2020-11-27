package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.C3POMicroservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {
    final int SOME_SIZE = 7;
    private MessageBus bus;
    private MicroService receiver;
    private MicroService sender;

    @BeforeEach
    void setUp() {
        bus = new MessageBusImpl();
        receiver = new C3POMicroservice();
        bus.register(receiver); // check register work
    }

    @Test
    void complete() {
        //prepare
        Event<Boolean> event = new AttackEvent();
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
    void sendBroadcast() throws InterruptedException {
        //prepare
        Broadcast broadcast = new TerminateBroadcast();
        MicroService receiver2 = new C3POMicroservice();
        bus.subscribeBroadcast(TerminateBroadcast.class, receiver);
        bus.subscribeBroadcast(TerminateBroadcast.class,receiver2);
        //act
        bus.sendBroadcast(broadcast);
        //assert
        assertEquals(broadcast,bus.awaitMessage(receiver));
        assertEquals(broadcast,bus.awaitMessage(receiver2));
    }

    @Test
    void sendEvent() throws InterruptedException {
        //prepare
        Event<Boolean> event = new AttackEvent();
        bus.subscribeEvent(AttackEvent.class, receiver); // also checks subscribeEvent
        //act
        bus.sendEvent(event); // checks sendEvent
        //assert
        Message result = bus.awaitMessage(receiver); // also checks awaitMessage;
        assertEquals(event,result);
    }
    //    @Test
//    public void awaitMessage() throws InterruptedException {
//        //prepare
//        Event<Boolean> event = new AttackEvent();
//        bus.subscribeEvent(AttackEvent.class, receiver);
//        bus.sendEvent(event);
//        //act
//        Message receivedEvent = bus.awaitMessage(receiver);
//        //assert
//        assertEquals(event,receivedEvent);
//    }
}
