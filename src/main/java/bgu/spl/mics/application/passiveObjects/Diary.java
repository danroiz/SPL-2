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
    public long HanSoloFinish; // indicates when HanSolo finished all of his attacks
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

    public static Diary getInstance(){
        return DiaryHolder.instance;
    }


}
