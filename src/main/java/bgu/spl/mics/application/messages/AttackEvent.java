package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    public AttackEvent(){
        System.out.println("dummy event");
    }
    Attack attack;
    public AttackEvent(Attack attack) {
        this.attack = attack;
    }
    public void act () {
        List<Integer> serials = attack.getSerials();
        System.out.println("attack event start of " + Thread.currentThread().getName());
        Ewoks.getInstance().acquireEwoks(serials);
        try {
            Thread.sleep(attack.getDuration());
        }
        catch (InterruptedException e){
            System.out.println("Thread: " + Thread.currentThread().getName() + " was interrupted");
        }

         Ewoks.getInstance().releaseEwoks(serials);
        System.out.println("attack event finish of " + Thread.currentThread().getName());

        Diary.getInstance().incrementAttack();
    }

}
