package bgu.spl.mics.application.passiveObjects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    public AtomicInteger totalAttacks = new AtomicInteger();
    public long HanSoloFinish;
    public long C3POFinish;
    public long R2D2Deactivate;
    public long LeiaTerminate;
    public long HanSoloTerminate;
    public long C3POTerminate;
    public long R2D2Terminate;
    public long LandoTerminate;


    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }

    /**
     * increment the number of attacks by one
     */
    public void incrementAttack () {
        totalAttacks.incrementAndGet();
    }

    /**
     * setters
     */
    public void logHanSoloFinish () {
        this.HanSoloFinish = System.currentTimeMillis();
    }
    public void logC3POFinish () {
        this.C3POFinish = System.currentTimeMillis();
    }
    public void logR2D2Deactivate () {
        this.R2D2Deactivate = System.currentTimeMillis();
    }
    public void logLeiaTerminate () {
        this.LeiaTerminate = System.currentTimeMillis();
    }
    public void logHanSoloTerminate () {
        this.HanSoloTerminate = System.currentTimeMillis();
    }
    public void logC3POTerminate () {
        this.C3POTerminate = System.currentTimeMillis();
    }
    public void logR2D2Terminate () {
        this.R2D2Terminate = System.currentTimeMillis();
    }
    public void logLandoTerminate () {
        this.LandoTerminate = System.currentTimeMillis();
    }

    /**
     *
     * @return the instance of the Diary singleton
     */
    public static Diary getInstance(){
        return DiaryHolder.instance;
    }

    /**
     *
     * for tests purposes only
     */
    public void resetNumberAttacks() {
        totalAttacks = new AtomicInteger(0);
    }
}
