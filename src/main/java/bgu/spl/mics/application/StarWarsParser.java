package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;

public class StarWarsParser {
    Attack[] attacks;
    long R2D2;
    long Lando;
    int Ewoks;
    public StarWarsParser(Attack[] attacks, long R2D2, long Lando, int Ewoks){
        this.attacks = attacks;
        this.R2D2 = R2D2;
        this.Lando = Lando;
        this.Ewoks = Ewoks;
    }
}
