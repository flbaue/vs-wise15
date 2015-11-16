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

    public static void main(String[] args) {
        new BankService().run();
    }

    private void run() {
        System.out.println("BankService is starting");

        //den Status von der Bank ausgeben
        get("/", this::root);

        get("/banks/:gameId/transfer/:transferId", this::getTransfer);

        //die Bank erstellen
        put("/", this::createBank);

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
    }

    private Object getTransfer(Request request, Response response) {
        Transaction transaction = null;
        String gameId = request.params(":gameId");
        Bank bank = null;
        ArrayList<Transaction> transList;
        try {
            transaction = gson.fromJson(request.body(), Transaction.class);
        } catch (JsonSyntaxException e) {
        }
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                bank = b;
            }
        }
        if (bank == null) {                  //TODO:falscher param bank id
            response.status(404);
            return null;
        }
        transList = bank.getTransactionList();
        for (Transaction t : transList) {
            if (transaction.getTransferId() == t.getTransferId()) {
                response.status(200);
                return gson.toJson(transaction);
            }
        }
        response.status(404);
        return null;
    }

    private Object createBank(Request request, Response response) throws Exception {
        Game game = null;
        Bank bank;
        int bankId;
        try {
            game = gson.fromJson(request.body(), Game.class);
        } catch (JsonSyntaxException e) {

        }

        if (game == null) {
            response.status(403);
            return null;
        }

        bank = new Bank(game);
        bankList.add(bank);
        response.status(201);
        return null;
    }

    private Object root(Request request, Response response) throws Exception {
        return null;
    }

    private Object transferMoneyfromAccount(Request request, Response response) {
        Transaction transaction = null;
        Player playerTo;
        Player playerFrom;
        boolean bfrom = false;
        boolean bto = false;
        Game game;
        String from;
        String to;
        BigDecimal amount;
        String reason;

        reason = request.body();

        from = request.params(":from");
        to = request.params(":to");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(from, to, reason, new Event());
        for (Bank b : bankList) {
            if (b.getBankId().equals(to)) {
                b.addTransaction(transaction);
                game = b.getGame();
                playerTo = game.getPlayerById(to);
                for(Account a: b.getAccountList()) {
                    if (a.getPlayer().equals(playerTo)) {
                        a.setSaldo(a.getSaldo().add(amount));
                    }
                }
                bto = true;

            }
            if (b.getBankId().equals(from)) {
                b.addTransaction(transaction);
                game = b.getGame();
                playerFrom = game.getPlayerById(from);
                for(Account a: b.getAccountList()) {
                    if (a.getPlayer().equals(playerFrom)) {
                        a.setSaldo(a.getSaldo().subtract(amount));
                    }
                }
                bfrom = true;
            }
        }
        if (bfrom && bto) {
            response.status(200);
            return null;
        }
        response.status(403);
        return null;
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
            return null;
        }
        from = request.params(":from");
        gameId = request.params(":gameId");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(from, gameId, reason, new Event());
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                b.addTransaction(transaction);
                game = b.getGame();
                player = game.getPlayerById(from);
                for(Account a: b.getAccountList()) {
                    if (a.getPlayer().equals(player)) {
                        a.setSaldo(a.getSaldo().subtract(amount));
                    }
                }
                response.status(201);
                return null;
            }
        }
        response.status(403);
        return null;
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
            return null;
        }
        gameId = request.params(":gameId");
        to = request.params(":to");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(gameId, to, reason, new Event());
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                b.addTransaction(transaction);
                game = b.getGame();
                player = game.getPlayerById(to);
                for(Account a: b.getAccountList()) {
                    if (a.getPlayer().equals(player)) {
                        a.setSaldo(a.getSaldo().add(amount));
                    }
                }
                response.status(201);
                return null;
            }
        }
        response.status(403);
        return null;
    }

    private BigDecimal getAccountBalance(Request request, Response response) {
        String playerId;
        String gameId;
        BigDecimal saldo;
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
            return null;
        }
        player = account.getPlayer();
        for (Bank b : bankList) {
            if (b.getBankId().equals(id)) {
                bank = b;
                for (Account a : b.getAccountList())
                    if (a.getPlayer().equals(player)) {
                        response.status(409);
                        return null;
                    }
            }
        }
        bank.addAccount(account);
        response.status(201);
        return null;
    }

}
