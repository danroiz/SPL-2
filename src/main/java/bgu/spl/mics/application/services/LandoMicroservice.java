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

    public LandoMicroservice() {
        super("Lando");
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
        }); // subscribe to termination broadcast
        subscribeEvent(BombDestroyerEvent.class, (bombDestroyerEvent) -> {

            try {
                Thread.sleep(bombDestroyerEvent.getDestroyTime());
            }
            catch (InterruptedException ignored) {
                //       System.out.println("Thread: " + Thread.currentThread().getName() + " was interrupted");
            }
            complete(bombDestroyerEvent,true);
        });

        //  System.out.println("LANDO MS: init: --ENTERING THE DOWNLATCH COUNTING to the subscribe event--");
        LatchSingleton.getDestroyLatch().countDown();
    }
    @Override
    protected void close() {
        Diary.getInstance().logLandoTerminate();
    }
}
