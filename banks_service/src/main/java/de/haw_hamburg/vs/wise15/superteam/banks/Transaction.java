package de.haw_hamburg.vs.wise15.superteam.banks;

/**
 * Created by masha on 16.11.15.
 */

public class Transaction {

    private String transferId;
    private String from;
    private String to;
    private String reason;
    private Event event;

    public Transaction(String from, String to, String reason, Event event) {
        this.from= from;
        this.to = to;
        this.reason = reason;
        this.event = event;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}
