package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by masha on 16.11.15.
 */
/*

(Lokale) Transaktion: Achten Sie darauf, dass ein Geldtransfer immer als eine Transaktion zu verstehen ist:
entweder wird diese ganz – oder gar nicht – ausgeführt.
Frage: Wie könnte man eine at-most-once Fehlersemantik implementieren?
 */
public class BankService {

    private Gson gson = new Gson();
    private ArrayList<Bank> bankList;
    private int transferId = 0;

    public static void main(String[] args) {
        new BankService().run();
    }

    private void run() {
        System.out.println("BankService is starting");

        //den Status von der Bank ausgeben
        get("/", this::root);

        //den Status von der Bank ausgeben
        put("/banks", this::createBank);

        get("/banks/:gameId/transfer/:transferId", this::getTransfer);

        //die Bank erstellen
        post("/banks", this::createBank);

        //ein Konto erstellen
        post("/banks/:gameid/players", this::createAccount);


        //Kontostand abfragen
        get("/banks/:gameid/players/:playerid", this::getAccountBalance);

        //Geld von der Bank überweisen
        post("/banks/:gameid/transfer/to/:to/:amount", this::transferMoneyfromBank);

        //Geld einziehen
        post("/banks/:gameid/transfer/from/:from/:amount", this::transferMoneytoBank);

        //Geld von einem zu anderen Konto übertragen
        post("/banks/:gameid/transfer/from/:from/to/:to/:amount", this::transferMoneyfromAccount);

        //transaktion bestenaetigen
        post("/banks/:gameid/transfer/:amount/:transferId", this::transBesaetigen);


    }

    private Object transBesaetigen(Request request, Response response) {
        String transId = request.params(":transferId");
        String gameId = request.params(":gameId");
        BigDecimal amount = new BigDecimal(request.params(":amount"));
        Bank bank = null;
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                bank = b;
            }
        }
        for (Transaction trans : bank.getTransListNichtBestaetigt()) {
            if (trans.getTransferId().equals(transId)) {
                transaktionDurchfuehren(trans, bank, response, amount);
            }
        }

        return "transBestatigt";
    }

    private Object getTransfer(Request request, Response response) {
        System.out.println("put banks");

        Transaction transaction = null;
        String gameId = request.params(":gameId");
        String transId = request.params(":transactionId");
        Bank bank = null;
        ArrayList<Transaction> transList;
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                bank = b;
            }
        }
        if (bank == null) {                  //TODO:falscher param bank id
            response.status(404);
            return "status bank nicht gefunden";
        }
        transList = bank.getTransactionList();
        for (Transaction t : transList) {
            if (transId.equals(t.getTransferId())) {
                response.status(200);
                return gson.toJson(t);
            }
        }
        response.status(404);
        return null;
    }

    private Object createBank(Request request, Response response) throws Exception {

        System.out.println("put banks");

        Game game = null;
        Bank bank;
        try {
            game = gson.fromJson(request.body(), Game.class);
        } catch (JsonSyntaxException e) {

        }

        System.out.println("put banks 2");

        if (game == null) {
            response.status(403);
            return "test";
        }

        bank = new Bank(game);
        bankList.add(bank);
        response.status(201);
        return "";
    }

    private String root(Request request, Response response) throws Exception {

        return "Hallo Bianca";
    }

    private Object transferMoneyfromAccount(Request request, Response response) {
        Transaction transaction = null;
        Player playerTo;
        Player playerFrom;
        Game game;
        String gameId;
        String from;
        String to;
        String reason;
        reason = request.body();
        gameId = request.params(":gameId");
        from = request.params(":from");
        to = request.params(":to");
        transaction = new Transaction(Integer.toString(transferId), from, to, reason, new Event());
        transferId++;
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                game = b.getGame();
                playerTo = game.getPlayerById(to);
                if (playerTo == null) {

                    return "playerTo nicht gefunden";
                }

                playerFrom = game.getPlayerById(from);
                if (playerFrom == null) {
                    return "playerTo nicht gefunden";
                }
                b.addTransNichtBestaetigt(transaction);

            }
            response.status(200);
            response.body(Integer.toString(transferId));
            response.header("Bitte aufrufen:","/banks/:gameid/transfer/:amount/:transferId");
            return "";
        }
        return "";
    }


    private Object transferMoneytoBank(Request request, Response response) {
        Transaction transaction = null;
        Player player;
        Game game;
        String from;
        String gameId;
        BigDecimal amount;
        String reason;
        try {
            reason = gson.fromJson(request.body(), String.class);
        } catch (JsonSyntaxException e) {
            return "json exception";
        }
        from = request.params(":from");
        gameId = request.params(":gameId");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(Integer.toString(transferId), from, "bank", reason, new Event());
        transferId++;
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                b.addTransNichtBestaetigt(transaction);
                response.status(201);
                response.body(transaction.getTransferId());
                response.header("Bitte aufrufen:","/banks/:gameid/transfer/:amount/:transferId");
                return "";
            }
        }
        return "";
    }

    private Object transferMoneyfromBank(Request request, Response response) {
        Transaction transaction = null;
        Player player;
        Game game;
        String gameId;
        String to;
        BigDecimal amount;
        String reason;
        Bank bank;
        int bankId;
        try {
            reason = gson.fromJson(request.body(), String.class);
        } catch (JsonSyntaxException e) {
            return "json exception";
        }
        gameId = request.params(":gameId");
        to = request.params(":to");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(Integer.toString(transferId), "bank", to, reason, new Event());
        transferId++;
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                b.addTransNichtBestaetigt(transaction);
                response.status(201);
                response.body(transaction.getTransferId());
                return "";
            }
        }
        response.status(403);
        response.header("Bitte aufrufen:","/banks/:gameid/transfer/:amount/:transferId");
        return "";
    }

    private BigDecimal getAccountBalance(Request request, Response response) {
        String playerId;
        String gameId;
        BigDecimal saldo;
        BigDecimal r = new BigDecimal(0);
        Player player = null;
        playerId = request.params(":playerId");
        gameId = request.params(":gameId");
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                player = b.getGame().getPlayerById(playerId);
                for (Account a : b.getAccountList()) {
                    if (a.getPlayer().equals(player)) {
                        saldo = a.getSaldo();
                        response.status(200);
                        return saldo;
                    }
                }
            }
        }

        response.status(404);
        return r;
    }

    private Object transaktionDurchfuehren(Transaction trans, Bank b, Response response, BigDecimal amount) {
        if (trans.getFrom().equals("bank")) {
            bezahlen(trans, b, trans.getTo(), response, amount);
        } else if (trans.getTo().equals("bank")) {
            bezahlen(trans, b, trans.getFrom(), response, amount.negate());
        } else if (!(trans.getTo().equals("bank")) && !(trans.getFrom().equals("bank"))) {
            bezahlen(trans, b, trans.getTo(), response, amount);
            bezahlen(trans, b, trans.getFrom(), response, amount.negate());
        } else {
            b.getTransListNichtBestaetigt().remove(trans);
            response.status(403);
            return "";
        }
        response.status(200);
        return "";
    }

    private Object bezahlen(Transaction trans, Bank b, String playerId, Response response, BigDecimal amount) {
        for (Account a : b.getAccountList()) {
            if (a.getPlayer().getPlayerId().equals(playerId)) {
                if (a.getSaldo().compareTo(amount.abs()) < 0) {
                    response.status(403);
                    b.getTransListNichtBestaetigt().remove(trans);
                    return "nicht genug saldo";
                }
                a.setSaldo(a.getSaldo().add(amount));
            }
        }
        return null;
    }

    //Player existiert schon, nur sein Account noch nicht
    private Object createAccount(Request request, Response response) {
        String id = request.params(":gameId");
        Player player;
        Account account;
        Bank bank = null;
        try {
            account = gson.fromJson(request.body(), Account.class);
        } catch (JsonSyntaxException e) {
            return "json exception";
        }
        player = account.getPlayer();
        for (Bank b : bankList) {
            if (b.getBankId().equals(id)) {
                bank = b;
                for (Account a : b.getAccountList())
                    if (a.getPlayer().equals(player)) {
                        response.status(409);
                        return "";
                    }
            }
        }
        bank.addAccount(account);
        response.status(201);
        return "";
    }

}
