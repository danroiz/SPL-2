package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.LandoSaysHiBroadcast;
import bgu.spl.mics.application.messages.R2D2SaysHiBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private final long deactivateTime;

    public R2D2Microservice(long duration) {
        super("R2D2");
        deactivateTime = duration;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(R2D2SaysHiBroadcast.class, (R2D2SaysHiBroadcast)-> System.out.println("R2D2 said hi to thread: " + this + " " + Thread.currentThread().getName()));
        subscribeBroadcast(LandoSaysHiBroadcast.class, (landoSaysHiBroadcast)-> System.out.println("Lando said hi to thread: " + this + " " + Thread.currentThread().getName()));

        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
        }); // subscribe to termination broadcast
        subscribeEvent(DeactivationEvent.class, (deactivationEvent) -> {
            try {
                Thread.sleep(deactivateTime);
            }

            catch (InterruptedException ignored) {
                //   System.out.println("Thread: " + Thread.currentThread().getName() + " was interrupted");
            }
            complete(deactivationEvent,true);
            Diary.getInstance().logR2D2Deactivate();
        });
        //    System.out.println("R2D2 MS: init: --ENTERING THE DOWNLATCH COUNTING to the subscribe event--");
        LatchSingleton.getDeactivateLatch().countDown();
    }
    @Override
    protected void close() {
        Diary.getInstance().logR2D2Terminate();
    }
}
