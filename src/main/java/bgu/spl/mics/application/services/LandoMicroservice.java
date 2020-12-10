package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private final long bombDestroyDuration;
    public LandoMicroservice(long duration) {
        super("Lando");
        bombDestroyDuration = duration;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast-> terminate());
        subscribeEvent(BombDestroyerEvent.class, (bombDestroyerEvent) -> {
            try {
                Thread.sleep(bombDestroyDuration);
            }
            catch (InterruptedException ignored) {
            }
            complete(bombDestroyerEvent,true);
        });
        LatchSingleton.getDestroyLatch().countDown();
    }

    @Override
    protected void close() {
        Diary.getInstance().logLandoTerminate();
    }
}
