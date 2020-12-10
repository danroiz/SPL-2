package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;

public class  StarWarsParser {
    private Attack[] attacks;
    private long R2D2;
    private long Lando;
    private int Ewoks;

    /**
     *
     * @param attacks - data of the duration and serials for an attack
     * @param R2D2 - time R2D2 need to deactivate the shield
     * @param Lando - time Lando need to destroy the planet
     * @param Ewoks - number of Ewoks in the application
     */
    public StarWarsParser(Attack[] attacks, long R2D2, long Lando, int Ewoks){
        this.attacks = attacks;
        this.R2D2 = R2D2;
        this.Lando = Lando;
        this.Ewoks = Ewoks;
    }

    /**
     * getters
     */
    public Attack[] getAttacks () { return attacks; }
    public long getR2D2 () { return R2D2; }
    public long getLando () { return Lando; }
    public int getEwoks() { return Ewoks; }
}
