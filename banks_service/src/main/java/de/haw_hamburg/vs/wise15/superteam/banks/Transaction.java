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

    public Transaction(String transferId, String from, String to, String reason, Event event) {
        this.transferId=transferId;
        this.from= from;
        this.to = to;
        this.reason = reason;
        this.event = event;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}
