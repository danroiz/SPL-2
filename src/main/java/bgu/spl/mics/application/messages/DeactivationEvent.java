package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean>{
    long deactivationTime;

    /**
     *
     * @param deactivationTime the amount of time needed to deactivate the shield
     */
    public DeactivationEvent (long deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    /**
     *
     * @return the amount of time needed to deactivate the shield
     */
    public long getDeactivationTime(){
        return deactivationTime;
    }
}
