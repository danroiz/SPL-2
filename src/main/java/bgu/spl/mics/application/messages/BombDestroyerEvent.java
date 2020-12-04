package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private final long destroyTime;
    public BombDestroyerEvent(long destroyTime) {
        this.destroyTime = destroyTime;
    }

    @Override
    public void act() {
        try {
            Thread.sleep(destroyTime);
        }
        catch (InterruptedException ignored) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " was interrupted");
        }

    }
}
