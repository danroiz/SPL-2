package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Diary;

public class DeactivationEvent implements Event<Boolean>{

    long deactivationTime;
    public DeactivationEvent (long deactivationTime) {
        this.deactivationTime = deactivationTime;
    }
    @Override
    public void act() {
        try {
            Thread.sleep(deactivationTime);
        }
        catch (InterruptedException ignored) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " was interrupted");
        }
    }
}
