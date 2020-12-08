package bgu.spl.mics.application;

import java.util.concurrent.CountDownLatch;

public class LatchSingleton {
    /**
     * initializing the LatchSingleton fields
     */
    public static void initialize() {
        LatchSingletonHolder.reset();
    }

    private static class LatchSingletonHolder{
        private static final int NUMBER_OF_ATTACK_THREADS = 2;
        private static final int NUMBER_OF_DEACTIVATE_THREADS = 1;
        private static final int NUMBER_OF_DESTROY_THREADS = 1;
        private static CountDownLatch attackLatch = new CountDownLatch(NUMBER_OF_ATTACK_THREADS);
        private static CountDownLatch deactivateLatch = new CountDownLatch(NUMBER_OF_DEACTIVATE_THREADS);
        private static CountDownLatch destroyLatch = new CountDownLatch(NUMBER_OF_DESTROY_THREADS);

        private static void reset() {
            if (attackLatch.getCount() != NUMBER_OF_ATTACK_THREADS)
                attackLatch = new CountDownLatch(NUMBER_OF_ATTACK_THREADS);
            if (attackLatch.getCount() != NUMBER_OF_DEACTIVATE_THREADS)
                deactivateLatch = new CountDownLatch(NUMBER_OF_DEACTIVATE_THREADS);
            if (attackLatch.getCount() != NUMBER_OF_DESTROY_THREADS)
                destroyLatch = new CountDownLatch(NUMBER_OF_DESTROY_THREADS);
        }
    }

    /**
     * getters
     */

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
