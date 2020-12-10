package bgu.spl.mics.application.services;
import java.util.ArrayList;
import java.util.List;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast-> terminate());
        handleAttackEvents();
        handleDeactivationEvent();
        handleBombDestroyEvent();
        sendBroadcast(new TerminateBroadcast());
    }

    private void handleAttackEvents() {
        try {
            LatchSingleton.getAttackLatch().await();} // wait for all attack-event handlers threads will subscribe
        catch (InterruptedException ignored){
        }
        List<Future<Boolean>> attackFutures = new ArrayList<>();
        for (Attack attack : attacks)
            attackFutures.add(sendEvent(new AttackEvent(attack))); // send all the attack-events, and save the returned future
        for (Future<Boolean> future : attackFutures)  // Leia wait's until all attacks are over
            future.get();
    }

    private void handleDeactivationEvent() {
        try {
            if (LatchSingleton.getDeactivateLatch().getCount() > 0)
                LatchSingleton.getDeactivateLatch().await(); // make sure R2D2 is subscribed to deactivation event
        }
        catch (InterruptedException ignored){
        }
        Future<Boolean> R2D2Future= sendEvent(new DeactivationEvent());
        R2D2Future.get();
    }

    private void handleBombDestroyEvent() {
        try {
            if (LatchSingleton.getDestroyLatch().getCount() > 0)
                LatchSingleton.getDestroyLatch().await(); // make sure Lando is subscribed to bombdestroy event
        }
        catch (InterruptedException ignored){
        }
        Future<Boolean> destroyerFuture = sendEvent(new BombDestroyerEvent());
        destroyerFuture.get();
    }

    @Override
    protected void close() {
         Diary.getInstance().logLeiaTerminate();
    }
}
