package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	CountDownLatch latch;

	// this is dummy constructor for tests purposes
	public C3POMicroservice(){
        super("C3PO");
    }

    public C3POMicroservice(CountDownLatch latch) {
        super("C3PO");
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            //System.out.println("Thread: " + Thread.currentThread().getName() + " callback on terminate, time: " + new Timestamp(System.currentTimeMillis()).getTime());
            terminate();
            //System.out.println("TRYING TO TERMINATE C3PO===================================");
            Diary.getInstance().logC3POTerminate();

        }); // subscribe to termination broadcast
        subscribeEvent(AttackEvent.class, (attackEvent) -> {
            attackEvent.act();
            complete(attackEvent,true);
            Diary.getInstance().logC3POFinish();
        }); // (attackEvent) -> attackEvent.act()

        latch.countDown();
    }
}
