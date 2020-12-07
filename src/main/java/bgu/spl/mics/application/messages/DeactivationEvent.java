package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean>{

    long deactivationTime;
    public DeactivationEvent (long deactivationTime) {
        this.deactivationTime = deactivationTime;
    }
    public long getDeactivationTime(){return deactivationTime;}
}
