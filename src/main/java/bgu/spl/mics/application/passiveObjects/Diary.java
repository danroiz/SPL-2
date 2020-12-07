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
    public long startStamp = System.currentTimeMillis();
    public long HanSoloFinish; // indicates when HanSolo finished all of his attacks
    public long C3POFinish;
    public long R2D2Deactivate;
    public long LeiaTerminate;
    public long HanSoloTerminate;
    public long C3POTerminate;
    public long R2D2Terminate;
    public long LandoTerminate;

    public void resetNumberAttacks() {
        totalAttacks = new AtomicInteger(0);
    }

    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }
    public void incrementAttack () {
        totalAttacks.incrementAndGet();
    }
    public void logHanSoloFinish () {
        this.HanSoloFinish = System.currentTimeMillis()  - startStamp;
    }
    public void logC3POFinish () {
        this.C3POFinish = System.currentTimeMillis() - startStamp;
    }
    public void logR2D2Deactivate () {
        this.R2D2Deactivate = System.currentTimeMillis() - startStamp;
    }
    public void logLeiaTerminate () {
        this.LeiaTerminate = System.currentTimeMillis() - startStamp;
    }
    public void logHanSoloTerminate () {
        this.HanSoloTerminate = System.currentTimeMillis() - startStamp;
    }
    public void logC3POTerminate () {
        this.C3POTerminate = System.currentTimeMillis() - startStamp;
    }
    public void logR2D2Terminate () {
        this.R2D2Terminate = System.currentTimeMillis() - startStamp;
    }
    public void logLandoTerminate () {
        this.LandoTerminate = System.currentTimeMillis() - startStamp;
    }
    public static Diary getInstance(){
        return DiaryHolder.instance;
    }
    public void output () {
        System.out.println("Total Attacks= " + totalAttacks);
        System.out.println("HanSoloFinish = " + HanSoloFinish);
        System.out.println("C3POFinish = " + C3POFinish);
        System.out.println("R2D2Deactivate = " + R2D2Deactivate);
        System.out.println("LeiaTerminate = " + LeiaTerminate);
        System.out.println("HanSoloTerminate = " + HanSoloTerminate);
        System.out.println("C3POTerminate = " + C3POTerminate);
        System.out.println("R2D2Terminate = " + R2D2Terminate);
        System.out.println("LandoTerminate = " + LandoTerminate);
    }
}
