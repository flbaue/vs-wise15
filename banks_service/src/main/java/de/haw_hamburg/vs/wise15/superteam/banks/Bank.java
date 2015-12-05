package de.haw_hamburg.vs.wise15.superteam.banks;

import java.util.ArrayList;

/**
 * Created by masha on 16.11.15.
 */
public class Bank {

    private String bankId;
    private Game game;
    private ArrayList<Transaction> transactionList;
    private ArrayList<Transaction> transListNichtBestaetigt;
    private ArrayList<Account> accountList;

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }

    public Bank(Game game){
        this.transListNichtBestaetigt= new ArrayList<Transaction>();
        this.transactionList= new ArrayList<Transaction>();
        this.accountList = new ArrayList<Account>();
        this.bankId = game.getGameId();
       this.game = game;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionList;

    }

    public ArrayList<Transaction> getTransListNichtBestaetigt() {
        return transListNichtBestaetigt;
    }

    public void setTransListNichtBestaetigt(ArrayList<Transaction> transListNichtBestaetigt) {
        this.transListNichtBestaetigt = transListNichtBestaetigt;
    }

    public void setTransactionList(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }

    public void addTransNichtBestaetigt(Transaction transaction) {
        this.transListNichtBestaetigt.add(transaction);
    }

    public void addAccount(Account account) {
        this.accountList.add(account);
    }
}
