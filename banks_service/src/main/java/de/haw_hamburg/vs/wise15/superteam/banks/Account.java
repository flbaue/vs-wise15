package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.math.BigDecimal;

/**
 * Created by masha on 16.11.15.
 */
public class Account {
    Gson gson;

    private Player player;
    private BigDecimal saldo;


    public Account(String player, String saldo) {
        try {
            this.player = gson.fromJson(player, Player.class);
        }catch (JsonSyntaxException e){
            System.out.println("account create: JsonSyntaxException");
        }

        this.saldo = new BigDecimal(saldo);
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
