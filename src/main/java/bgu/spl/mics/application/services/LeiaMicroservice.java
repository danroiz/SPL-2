package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
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
	private final long R2D2;
	private long Lando;
	private CountDownLatch attackLatch, deactivateLatch, destroyLatch;
    public LeiaMicroservice(Attack[] attacks, long R2D2, long Lando, CountDownLatch attackLatch, CountDownLatch deactivateLatch, CountDownLatch destroyLatch) {
        super("Leia");
		this.attacks = attacks;
		this.R2D2 = R2D2;
		this.Lando = Lando;
		this.attackLatch = attackLatch;
		this.deactivateLatch = deactivateLatch;
		this.destroyLatch = destroyLatch;
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
            Diary.getInstance().logLeiaTerminate();
        }); // subscribe to termination broadcast // subscribe to termination broadcast

        List<Future<Boolean>> attackFutures = new ArrayList<>();
        try {
            attackLatch.await();}
        catch (InterruptedException ignored){
            System.out.println("Thread: Leia was interrupted during latch waiting");
        }

        for (Attack attack : attacks){
            attackFutures.add(sendEvent(new AttackEvent(attack))); // send all the attack-events, and save the returned future
        }
        boolean finishAllAttacks = true;
        for (Future<Boolean> future : attackFutures) { // Leia wait's until all attacks are over
            System.out.println(         "Future : " + future + " is weird");
            finishAllAttacks = finishAllAttacks & future.get();
            if (!finishAllAttacks) // DEBUG PURPOSE
                System.out.println("WTF future AttackEvent finished get but resolve was false");
        }
        System.out.println("FINISHED ALL THE ATTACKS");
        try {
            deactivateLatch.await();}
        catch (InterruptedException ignored){
            System.out.println("Thread: Leia was interrupted during latch waiting");
        }
        Future<Boolean> R2D2Future= sendEvent(new DeactivationEvent(R2D2));
        boolean R2D2Finished = R2D2Future.get();
        if (!R2D2Finished) System.out.println("WTF future RD2 finished get but resolved was false");

        try {
            destroyLatch.await();}
        catch (InterruptedException ignored){
            System.out.println("Thread: Leia was interrupted during latch waiting");
        }
        Future<Boolean> destroyerFuture = sendEvent(new BombDestroyerEvent(Lando));
        if (!destroyerFuture.get()) System.out.println("WTF future of Destroyer finished get but resolved was false");
        sendBroadcast(new TerminateBroadcast());

    	/*
    	// subscribe to all the shit
    	// send all the attack events
    	// for each future got from attack events, do future.get()
    	// send deactivation event to R2D2
    	//
    	 */
    }
}
