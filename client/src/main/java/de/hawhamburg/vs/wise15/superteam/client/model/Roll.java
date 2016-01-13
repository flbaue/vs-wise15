package de.hawhamburg.vs.wise15.superteam.client.model;

/**
 * Created by florian on 12.01.16.
 */
public class Roll {

    private int number;

    public Roll(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
