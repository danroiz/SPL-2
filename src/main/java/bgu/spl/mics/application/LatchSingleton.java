package bgu.spl.mics.application;

import java.util.concurrent.CountDownLatch;

public class LatchSingleton {
    public static void initialize() {
        LatchSingletonHolder.reset();
    }

    private static class LatchSingletonHolder{
        private static CountDownLatch attackLatch = new CountDownLatch(2);
        private static CountDownLatch deactivateLatch = new CountDownLatch(1);
        private static CountDownLatch destroyLatch = new CountDownLatch(1);

        private static void reset() {
            if (attackLatch.getCount() != 2)
                attackLatch = new CountDownLatch(2);
            if (attackLatch.getCount() != 1)
                deactivateLatch = new CountDownLatch(1);
            if (attackLatch.getCount() != 1)
                destroyLatch = new CountDownLatch(1);
        }
    }
    public static CountDownLatch getAttackLatch(){
        return LatchSingletonHolder.attackLatch;
    }
    public static CountDownLatch getDeactivateLatch(){
        return LatchSingletonHolder.deactivateLatch;
    }
    public static CountDownLatch getDestroyLatch(){
        return LatchSingletonHolder.destroyLatch;
    }
}
