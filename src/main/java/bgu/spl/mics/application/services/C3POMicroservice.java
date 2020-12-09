package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.LandoSaysHiBroadcast;
import bgu.spl.mics.application.messages.R2D2SaysHiBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import java.util.List;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {


	public C3POMicroservice(){
        super("C3PO");
    }



    @Override
    protected void initialize() {
        subscribeBroadcast(LandoSaysHiBroadcast.class, (LandoSaysHiBroadcast)-> System.out.println("Lando said hi to thread: " + this + " " + Thread.currentThread().getName()));
        subscribeBroadcast(R2D2SaysHiBroadcast.class, (R2D2SaysHiBroadcast)-> System.out.println("R2D2 said hi to thread: " + this + " " + Thread.currentThread().getName()));
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            //System.out.println("Thread: " + Thread.currentThread().getName() + " callback on terminate, time: " + new Timestamp(System.currentTimeMillis()).getTime());
            terminate();
            //System.out.println("TRYING TO TERMINATE C3PO===================================");

        }); // subscribe to termination broadcast
        subscribeEvent(AttackEvent.class, (attackEvent) -> {
            Attack attack = attackEvent.getAttack();
            List<Integer> serials = attack.getSerials();
            // System.out.println("- Attack Event class: act: start: " + Thread.currentThread().getName());
            Ewoks.getInstance().acquireEwoks(serials);
            try {
                //    System.out.println("+++++++entering sleeping for: " + attack.getDuration() + " Thread: " + Thread.currentThread().getName());
                Thread.sleep(attack.getDuration());
            }
            catch (InterruptedException e){
                //      System.out.println("- Attack Event: interrupt while act: " + Thread.currentThread().getName());
            }

            Ewoks.getInstance().releaseEwoks(serials);
            //    System.out.println("- Attack Event class: act: finish: " + Thread.currentThread().getName());

            Diary.getInstance().incrementAttack();
            complete(attackEvent,true);
            Diary.getInstance().logC3POFinish();
        });

        LatchSingleton.getAttackLatch().countDown();
    }
    @Override
    protected void close() {
        //System.out.println("TRYING TO TERMINATE C3PO===================================");
        Diary.getInstance().logC3POTerminate();
    }
}
