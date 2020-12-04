package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private CountDownLatch destroyLatch;
    public LandoMicroservice(CountDownLatch destroyLatch) {
        super("Lando");
        this.destroyLatch = destroyLatch;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
            Diary.getInstance().logLandoTerminate();
        }); // subscribe to termination broadcast
        subscribeEvent(BombDestroyerEvent.class, (bombDestroyerEvent) -> {
            bombDestroyerEvent.act();
            complete(bombDestroyerEvent,true);
        }); // (attackEvent) -> attackEvent.act()

        System.out.println("LANDO MS: init: --ENTERING THE DOWNLATCH COUNTING to the subscribe event--");
        destroyLatch.countDown();
    }
}
