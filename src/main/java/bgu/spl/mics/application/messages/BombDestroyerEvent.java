package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private final long destroyTime;

    /**
     *
     * @param destroyTime the amount of time needed to destroy the planet
     */
    public BombDestroyerEvent(long destroyTime) {
        this.destroyTime = destroyTime;
    }

    /**
     *
     * @return the amount of time needed to destroy the planet
     */
    public long getDestroyTime(){
        return destroyTime;
    }
}
