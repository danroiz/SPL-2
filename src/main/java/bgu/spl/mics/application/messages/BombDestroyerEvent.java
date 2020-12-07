package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private final long destroyTime;

    public BombDestroyerEvent(long destroyTime) {
        this.destroyTime = destroyTime;
    }

    public long getDestroyTime(){return destroyTime;}

}
