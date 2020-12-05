package bgu.spl.mics.application.passiveObjects;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> serials;
    final int duration;

    /**
     * Constructor.
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
        Collections.sort(serials); // Sort the serials to prevent deadlock on accquire Ewoks collection
    }
    public void act () throws InterruptedException {
        Ewoks.getInstance().acquireEwoks(serials);
        Thread.sleep(duration);
        Ewoks.getInstance().releaseEwoks(serials);
        if (Thread.currentThread().getName() == "C3PO")
            Diary.getInstance().logC3POFinish();
        if (Thread.currentThread().getName() == "HanSolo")
            Diary.getInstance().logHanSoloFinish();
    }
    public int getDuration() {return duration;}
    public List<Integer> getSerials(){return serials;}
}
