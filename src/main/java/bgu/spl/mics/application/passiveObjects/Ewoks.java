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



    public void initialize(int size) { // Main responsibility to first init Ewoks
        ewoks = new Vector<>(size);
        for (int i = 0;i < size;i++) {
            ewoks.add(i, new Ewok(i + 1));
        }
    }

    public static Ewoks getInstance() { // classic singleton
        return EwoksHolder.instance;
    }

    public void acquireEwoks (List<Integer> serials) {
        Collections.sort(serials);
        for (Integer serial: serials) {
            ewoks.get(serial-1).acquire();
        }
    }
    public void releaseEwoks (List<Integer> serials) {
        Collections.sort(serials);
        for (Integer serial: serials) {
            ewoks.get(serial-1).release();
        }
    }
}
