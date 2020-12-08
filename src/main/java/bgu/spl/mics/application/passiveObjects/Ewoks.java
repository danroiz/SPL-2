package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.StarWarsParser;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 *
 */
public class Ewoks {

    private Vector<Ewok> ewoks;

    private static class EwoksHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {
        ewoks = new Vector<>();
    }


    /**
     * initializing the ewoks list
     * @param size the number of ewoks in the application
     */
    public void initialize(int size) {
        ewoks = new Vector<>(size);
        for (int i = 0;i < size;i++) {
            ewoks.add(i, new Ewok(i + 1));
        }
    }

    /**
     *
     * @return the Ewoks singleton instance
     */
    public static Ewoks getInstance() { // classic singleton
        return EwoksHolder.instance;
    }

    /**
     * acquiring the ewoks from serials list
     * @param serials the id's of the desired ewoks
     */
    public void acquireEwoks (List<Integer> serials) {
        for (Integer serial: serials) {
            ewoks.get(serial-1).acquire();
        }
    }

    /**
     * releasing the ewoks from serials list
     * @param serials the id's of the desired ewoks
     */
    public void releaseEwoks (List<Integer> serials) {
        for (Integer serial: serials) {
            ewoks.get(serial-1).release();
        }
    }
}
