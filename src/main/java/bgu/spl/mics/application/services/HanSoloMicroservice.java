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
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice( ) {
        super("Han");
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast-> terminate());
        subscribeEvent(AttackEvent.class, (attackEvent) -> {
            Attack attack = attackEvent.getAttack();
            List<Integer> serials = attack.getSerials();
            Ewoks.getInstance().acquireEwoks(serials);
            try {
                Thread.sleep(attack.getDuration());
            }
            catch (InterruptedException ignored){
            }
            Ewoks.getInstance().releaseEwoks(serials);
            Diary.getInstance().incrementAttack();
            complete(attackEvent,true);
            Diary.getInstance().logHanSoloFinish();
        });
        LatchSingleton.getAttackLatch().countDown();
    }

    @Override
    protected void close() {
        Diary.getInstance().logHanSoloTerminate();
    }
}
