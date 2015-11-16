package de.haw_hamburg.vs.wise15.superteam.banks;

import java.math.BigDecimal;

/**
 * Created by masha on 16.11.15.
 */
public class Account {

    private Player player;
    private BigDecimal saldo;


    public Account(Player player) {
        this.player = player;
        this.saldo = new BigDecimal(200);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
