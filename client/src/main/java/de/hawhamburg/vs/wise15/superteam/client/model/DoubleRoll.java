package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 13.01.16.
 */
public class DoubleRoll {
    public Roll roll1;
    public Roll roll2;

    public DoubleRoll(Roll[] rolls) {
        roll1 = rolls[0];
        roll2 = rolls[1];
    }
}
