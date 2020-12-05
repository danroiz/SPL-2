package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class Output {
    public int totalAttacks;
    public long HanSoloFinish; // indicates when HanSolo finished all of his attacks
    public long C3POFinish;
    public long R2D2Deactivate;
    public long LeiaTerminate;
    public long HanSoloTerminate;
    public long C3POTerminate;
    public long R2D2Terminate;
    public long LandoTerminate;

    public Output() {
        Diary diary = Diary.getInstance();
        this.HanSoloFinish = diary.HanSoloFinish;
        this.C3POFinish = diary.C3POFinish;
        this.R2D2Deactivate = diary.R2D2Deactivate;
        this.LeiaTerminate = diary.LeiaTerminate;
        this.HanSoloTerminate = diary.HanSoloTerminate;
        this.C3POTerminate = diary.C3POTerminate;
        this.R2D2Terminate = diary.R2D2Terminate;
        this.LandoTerminate = diary.LandoTerminate;
        this.totalAttacks = diary.totalAttacks.intValue();

    }
}
