package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	volatile boolean available;

    /**
     * Ewok Constructor
     * @INV when new Ewok created, he's available
     * @param serialNumber the ID of of the Ewok
     */
    public Ewok(int serialNumber){
        this.serialNumber = serialNumber;
        available = true;
    }

    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
		while (!available){
		    try{
                System.out.println("---- EWOK ACQUIRE: START Thread :" + Thread.currentThread().getName() + " entered wait, tried to acquire: " + this.serialNumber);
		        wait();}
		    catch (InterruptedException e) {
                System.out.println("---- EWOK ACQUIRE --PROBLEM --: Thread: " + Thread.currentThread().getName() + " interupted");
            }
        }
    //    System.out.println("Thread: " + Thread.currentThread().getName() + " acquire ewok: " + this.serialNumber);
		available = false;
        System.out.println("---- EWOK ACQUIRE: FINISH: Thread :" + Thread.currentThread().getName() + " success acquire: " + this.serialNumber);
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
        if (available)
            System.out.println("---- EWOK RELEASE: --PROBLEM-- Thread :" + Thread.currentThread().getName() + " trying to release ewok " + this.serialNumber + " BUT is already released");
        System.out.println("---- EWOK RELEASE: FINISH Thread :" + Thread.currentThread().getName() + " success release ewok " + this.serialNumber);
        available = true;
    	notifyAll();
    }
}
