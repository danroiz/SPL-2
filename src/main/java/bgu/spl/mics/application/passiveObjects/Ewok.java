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
                System.out.println("Thread :" + Thread.currentThread().getName() + " entered wait");
		        wait();}
		    catch (InterruptedException e) {
                System.out.println("Thread: " + Thread.currentThread().getName() + " interupted");
            }
        }
        System.out.println("Thread: " + Thread.currentThread().getName() + " acquire ewok: " + this.serialNumber);
		available = false;
        System.out.println("                                                                        available is: " + available + " ewok: " + this.serialNumber);
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
        if (available)
            System.out.println("Thread: " + Thread.currentThread().getName() + " released an ewok " + this.serialNumber + " who was already released WTF22222");
        System.out.println("Thread: " + Thread.currentThread().getName() + " is realsing ewok: " + this.serialNumber);
        available = true;
    	notifyAll();
    }
}
