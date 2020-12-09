package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.LatchSingleton;
import bgu.spl.mics.application.messages.AttackEvent;
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
public class TerminateMicroservice extends MicroService {

    long startTimeStamp = System.currentTimeMillis();
    public TerminateMicroservice(){
        super("Terminator");
    }



    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast)-> {
            terminate();
        }); // subscribe to termination broadcast
        try {
            if (LatchSingleton.getTerminateLatch().getCount() > 0) {
                System.out.println("Terminator is entering wait for leia threads.");
                LatchSingleton.getTerminateLatch().await();
            }
        }
        catch (InterruptedException ignored){
        }

        System.out.println("Terminator sending termination broadcast.");
        sendBroadcast(new TerminateBroadcast());
    }
    @Override
    protected void close() {
        System.out.println("TERMINATOR TERMINATE IN TIME: " + ( System.currentTimeMillis() - startTimeStamp));
    }
}
