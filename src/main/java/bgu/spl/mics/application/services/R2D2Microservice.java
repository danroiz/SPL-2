package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.DeactivationEvent;
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
        subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast-> terminate());
        subscribeEvent(DeactivationEvent.class, (deactivationEvent) -> {
            try {
                Thread.sleep(deactivateTime);
            }
            catch (InterruptedException ignored) {
            }
            complete(deactivationEvent,true);
            Diary.getInstance().logR2D2Deactivate();
        });
        LatchSingleton.getDeactivateLatch().countDown();
    }

    @Override
    protected void close() {
        Diary.getInstance().logR2D2Terminate();
    }
}
