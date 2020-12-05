package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private CountDownLatch deactivateLatch;
    public R2D2Microservice(CountDownLatch deactivateLatch) {
        super("R2D2");
        this.deactivateLatch = deactivateLatch;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
            Diary.getInstance().logR2D2Terminate();
        }); // subscribe to termination broadcast
        subscribeEvent(DeactivationEvent.class, (DeactivationEvent) -> {
            DeactivationEvent.act();
            complete(DeactivationEvent,true);
            Diary.getInstance().logR2D2Deactivate();
        });
    //    System.out.println("R2D2 MS: init: --ENTERING THE DOWNLATCH COUNTING to the subscribe event--");
        deactivateLatch.countDown();
    }
}
