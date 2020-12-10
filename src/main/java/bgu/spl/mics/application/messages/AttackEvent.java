package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {

    private Attack attack;

    /**
     *
     * @param attack the data of the attack: duration and serials.
     */
    public AttackEvent(Attack attack) {
        this.attack = attack;
    }

    /**
     * Dummy constructor for the Unit-Tests
     */
    public AttackEvent() { }

    /**
     *
     * @return Attack object
     */
    public Attack getAttack(){
        return attack;
    }


}
