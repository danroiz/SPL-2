package bgu.spl.mics.application.services;


import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.CountDownLatch;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    CountDownLatch latch;
    public HanSoloMicroservice(CountDownLatch latch) {
        super("Han");
        this.latch = latch;
    }


    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
            Diary.getInstance().logHanSoloTerminate();
        }); // subscribe to termination broadcast
        subscribeEvent(AttackEvent.class, (attackEvent) -> {
            attackEvent.act();
            complete(attackEvent,true);
            System.out.println("Thread: ");
            Diary.getInstance().logHanSoloFinish();
        });
        latch.countDown();
    }

}
