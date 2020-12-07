package bgu.spl.mics.application.passiveObjects;
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

    private Ewoks (int numberOfEwoks) {
        ewoks = new Vector<Ewok>(numberOfEwoks);
        for (int i = 0;i < numberOfEwoks;i++) {
            ewoks.add(i, new Ewok(i + 1));
        }
    }
    private static Ewoks instance;
    public static void initialize(int size) { // Main responsibility to first init Ewoks
        if (instance == null)
           instance = new Ewoks(size);
      //  else
    //        System.out.println("You cannot initialize Ewoks Twice");
    }

    public static Ewoks getInstance() { // classic singleton
        if (instance == null) {
            System.out.println("first init to Ewoks was not in main thread"); // sanity check. won't happen in our application.
        }
        return instance;

    }

    public static void reset() {
        instance = null;
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
