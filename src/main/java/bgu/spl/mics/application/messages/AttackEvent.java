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
        System.out.println("- Attack Event class: act: start: " + Thread.currentThread().getName());
        Ewoks.getInstance().acquireEwoks(serials);
        try {
            System.out.println("+++++++entering sleeping for: " + attack.getDuration() + " Thread: " + Thread.currentThread().getName());
            Thread.sleep(attack.getDuration());
        }
        catch (InterruptedException e){
            System.out.println("- Attack Event: interrupt while act: " + Thread.currentThread().getName());
        }

         Ewoks.getInstance().releaseEwoks(serials);
        System.out.println("- Attack Event class: act: finish: " + Thread.currentThread().getName());

        Diary.getInstance().incrementAttack();
    }

}
