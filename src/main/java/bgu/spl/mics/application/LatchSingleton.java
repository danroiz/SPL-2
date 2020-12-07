package bgu.spl.mics.application;

import java.util.concurrent.CountDownLatch;

public class LatchSingleton {
    public static void reset() {
        LatchSingletonHolder.reset();
    }

    private static class LatchSingletonHolder{
        private static CountDownLatch attackLatch = new CountDownLatch(2);
        private static CountDownLatch deactivateLatch = new CountDownLatch(1);
        private static CountDownLatch destroyLatch = new CountDownLatch(1);

        public static void reset() {
            attackLatch = new CountDownLatch(2);
            deactivateLatch = new CountDownLatch(1);
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
