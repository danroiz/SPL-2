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
	private final long R2D2;
	private long Lando;

    public LeiaMicroservice(Attack[] attacks, long R2D2, long Lando) {
        super("Leia");
		this.attacks = attacks;
		this.R2D2 = R2D2;
		this.Lando = Lando;
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
        }); // subscribe to termination broadcast // subscribe to termination broadcast

        List<Future<Boolean>> attackFutures = new ArrayList<>();
        try {
            LatchSingleton.getAttackLatch().await();}
        catch (InterruptedException ignored){
        //    System.out.println("Thread: Leia was interrupted during latch waiting");
        }

        for (Attack attack : attacks){
         //   System.out.println("++++++++++Leia is sending attack event: " + attack.getDuration());
            attackFutures.add(sendEvent(new AttackEvent(attack))); // send all the attack-events, and save the returned future
        }
        boolean finishAllAttacks = true;
        for (Future<Boolean> future : attackFutures) { // Leia wait's until all attacks are over
       //     System.out.println(                                                                    "Leia MS: init: checking the future.get : " + future);
            finishAllAttacks = finishAllAttacks & future.get();
         //   if (!finishAllAttacks) // DEBUG PURPOSE
         //       System.out.println("WTF future AttackEvent finished get but resolve was false");
        }
    //    System.out.println("------ All The Attacks Finished: waiting to DEACTIVATION LATCH");
        try {
      //      System.out.println("------ All The Attacks Finished: ---TRY CATCH LOOP---");
            if (LatchSingleton.getDeactivateLatch().getCount() > 0)
                LatchSingleton.getDeactivateLatch().await();
        }
        catch (InterruptedException ignored){
     //       System.out.println("Thread: Leia was interrupted during latch waiting");
        }

     //   System.out.println("******* R2D2 successfully subscribed to deactivation event *******");


        Future<Boolean> R2D2Future= sendEvent(new DeactivationEvent(R2D2));
        boolean R2D2Finished = R2D2Future.get();
      //  System.out.println("******* R2D2 successfully FINISHED HIS MISSION: DEACTIVATE *******");

     //   if (!R2D2Finished) System.out.println("***** PROBLEM **** WTF future RD2 finished get but resolved was false");

   //     System.out.println("******* Before starting to wait to Lando destroy LUCH *******");
        try {
      //      System.out.println("------ Waiting for Lando to downlatch: ---TRY CATCH LOOP---");
            if (LatchSingleton.getDestroyLatch().getCount() > 0)
                LatchSingleton.getDestroyLatch().await();
        }
        catch (InterruptedException ignored){
    //        System.out.println("Thread: Leia was interrupted during latch waiting");
        }
     //   System.out.println("******* Lando successfully subscribed to BombDestroy event *******");
    //    System.out.println("******* Leia will send BombDestroy Event to Lando *******");
        Future<Boolean> destroyerFuture = sendEvent(new BombDestroyerEvent(Lando));
   //     System.out.println("******* Leia successfully sended BombDestroy Event to Lando *******");
          boolean LandoFinished = destroyerFuture.get();
   //     if (!destroyerFuture.get()) System.out.println("WTF future of Destroyer finished get but resolved was false");
   //     System.out.println("******* Leia want them all to TERMINATE *******");
        sendBroadcast(new TerminateBroadcast());
    //    System.out.println("******* MIS CHANG *******");

    	/*
    	// subscribe to all the shit
    	// send all the attack events
    	// for each future got from attack events, do future.get()
    	// send deactivation event to R2D2
    	//
    	 */
    }
    @Override
    protected void close() {
         Diary.getInstance().logLeiaTerminate();
    }
}
