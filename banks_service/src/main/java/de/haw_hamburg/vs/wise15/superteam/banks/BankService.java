package de.haw_hamburg.vs.wise15.superteam.banks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import spark.Request;
import spark.Response;

import javax.net.ssl.SSLContext;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static spark.Spark.*;

public class BankService {

    private String ip;
    private Gson gson = new Gson();
    private ArrayList<Bank> bankList = new ArrayList<Bank>();
    private AtomicInteger transferId = new AtomicInteger(0);

    public static void main(String[] args) {
        new BankService().run();
    }

    public void run() {
        //anmeldung
        //https://vs-docker.informatik.haw-hamburg.de/ports/8053/services
        register();

        System.out.println("BankService is starting");

        //den Status von der Bank
        //ok
        get("/", this::root);
        //post("/example/game", this::gameerst);
        //post("/example/player", this::playererst);

        //die Bank erstellen
        post("/banks", this::createBank);

        //places a banks
        //ok
        put("/banks/:gameid", this::createBank);

        //Gets a banks
        //ok
        get("/banks/:gameid", this::getBank);

        //ok
        get("/banks/:gameid/players", this::getPlayers);

        //ein Konto erstellen
        //ok
        post("/banks/:gameid/players", this::createAccount);

        //Kontostand abfragen
        //ok
        get("/banks/:gameid/players/:playerid", this::getAccountBalance);

        //Geld von der Bank überweisen
        //
        post("/banks/:gameid/transfer/to/:to/:amount", this::transferMoneyfromBank);

        //Geld einziehen
        //
        post("/banks/:gameid/transfer/from/:from/:amount", this::transferMoneytoBank);

        //Geld von einem zu anderen Konto übertragen
        //ok
        post("/banks/:gameid/transfer/from/:from/to/:to/:amount", this::transferMoneyfromAccount);

        //Ok
        get("/banks/:gameid/transfers", this::getTransfer);

        //ok
        get("/banks/:gameId/transfer/:transferId", this::getTransferById);

        //transaktion bestenaetigen
        //post("/banks/:gameid/transfer/:amount/:transferId", this::transBesaetigen);
    }

    private void register() {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        JsonObject json = new JsonObject();
        json.addProperty("name", "SuperTeamBanksService");
        json.addProperty("description", "BankService von SuperTeam");
        json.addProperty("service","banks");
        json.addProperty("uri","https://vs-docker.informatik.haw-hamburg.de/cnt/"+ ip+"/4567");
        try {
            SSLContext sslcontext = null;
            try {
                sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            Unirest.setHttpClient(httpclient);
            //sout
            System.out.println(json.toString());
            HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.post("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
                    .header("Content-Type", "application/json")
                    .body(json.toString()).asJson();
            System.out.println(jsonNodeHttpResponse.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    // http://localhost:4567/banks/:gameid/players
    private Object getPlayers(Request request, Response response) {
        String gameId = request.params(":gameId");
        for(Bank b : bankList){
            if (b.getBankId().equals(gameId)){
                response.status(200);
                return gson.toJson(b.getGame().getPlayerList());
            }
        }
        return gson.toJson(new ArrayList<>());
        //return "gameId falsch übergeben!?";
    }

    //get http://localhost:4567/banks/:gameId/transfer/:transferId
    public Object getTransferById(Request request, Response response) {
        String gameId = request.params(":gameId");
        String transId = request.params(":transferId");
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                for (Transaction t : b.getTransactionList()) {
                    if (transId.equals(t.getTransferId())) {
                        response.status(200);
                        return gson.toJson(t);
                    }
                }
            }
        }

        response.status(404);
        return null;
    }

    // get http://localhost:4567/banks/:gameid
    public Object getBank(Request request, Response response) {
        String gameId = request.params(":gameId");
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                response.status(200);
                return gson.toJson(b);
            }
        }
        response.status(404);
        return null;
    }

    public Object gameerst(Request request, Response response) {
        Game game = new Game("123", new ArrayList<String>(), new Component());
        response.body(gson.toJson(game));
        return gson.toJson(game);
    }

    public Object playererst(Request request, Response response) {
        Player player = new Player("1", "Maria", "uri", new Place("Schönbrunn"), 1);
        response.body(gson.toJson(player));
        return gson.toJson(player);
    }

/*    public Object transBesaetigen(Request request, Response response) {
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
    }*/

    //get http://localhost:4567/banks/:gameid/transfers
    public Object getTransfer(Request request, Response response) {
        String gameId = request.params(":gameId");
        ArrayList<Transaction> transList = new ArrayList<Transaction>();
        for (Bank b : bankList) {
            if (gameId.equals(b.getBankId())) {
                transList = b.getTransactionList();
                response.status(200);
                return gson.toJson(transList);
            }
        }
        response.status(404);
        return null;
    }

    //post http://localhost:4567/banks
    public Object createBank(Request request, Response response) throws Exception {

        System.out.println("put banks");

        Game game = null;
        Bank bank;
        try {

            game = gson.fromJson(request.body(), Game.class);
        } catch (JsonSyntaxException e) {
            return "JsonSyntaxException";
        }

        System.out.println("put banks 2");

        if (game == null) {
            response.status(403);
            return "game is null";
        }

        bank = new Bank(game);
        bankList.add(bank);
        response.status(201);
        return "Die Bank wurde erstellt";
    }

    public String root(Request request, Response response) throws Exception {

        return "BankService answered: pong";
    }

    //post http://localhost:4567/banks/:gameid/transfer/from/:from/to/:to/:amount
    public Object transferMoneyfromAccount(Request request, Response response) {
        Transaction transaction = null;
        Player playerTo;
        Player playerFrom;
        BigDecimal amount;
        Game game;
        String gameId;
        String from;
        String to;
        gameId = request.params(":gameId");
        from = request.params(":from");
        to = request.params(":to");
        amount = new BigDecimal(request.params(":amount"));
        //todo syncronized
        transaction = new Transaction(Integer.toString(transferId.incrementAndGet()), from, to, request.body(), new Event(), amount);

        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                game = b.getGame();
                playerTo = game.getPlayerById(to);
                if (playerTo == null) {

                    return "playerTo nicht gefunden";
                }

                playerFrom = game.getPlayerById(from);
                if (playerFrom == null) {
                    return "playerFrom nicht gefunden";
                }
                transaktionDurchfuehren(transaction, b, response);
            }
            return "Geld wurde erfolgreich von einem player zu anderem player überwiesen";
        }
        return "";
    }

    //post http://localhost:4567/banks/:gameid/transfer/from/:from/:amount
    public Object transferMoneytoBank(Request request, Response response) {
        Transaction transaction = null;
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
        transaction = new Transaction(Integer.toString(transferId.incrementAndGet()), from, "bank", reason, new Event(), amount);
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                //abziehen von playerFrom
                transaktionDurchfuehren(transaction, b, response);
                return "Geld wurde von einem Player an die Bank überwiesen";
            }
        }
        response.status(403);
        return "";
    }

    //post http://localhost:4567/banks/:gameid/transfer/to/:to/:amount
    //in body reason: z.B.: "Schulden begleichen"
    public Object transferMoneyfromBank(Request request, Response response) {
        Transaction transaction = null;
        String gameId;
        String to;
        BigDecimal amount;
        String reason;
        try {
            reason = gson.fromJson(request.body(), String.class);
        } catch (JsonSyntaxException e) {
            return "json exception";
        }
        gameId = request.params(":gameId");
        to = request.params(":to");
        amount = new BigDecimal((request.params(":amount")));
        transaction = new Transaction(Integer.toString(transferId.incrementAndGet()), "bank", to, reason, new Event(), amount);
        for (Bank b : bankList) {
            if (b.getBankId().equals(gameId)) {
                transaktionDurchfuehren(transaction, b, response);
                return "Geld wurde von der Bank zu einem Player überwiesen";
            }
        }
        response.status(403);
        return "";
    }

    //get http://localhost:4567/banks/:gameid/players/:playerid
    public BigDecimal getAccountBalance(Request request, Response response) {
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

    public void transaktionDurchfuehren(Transaction trans, Bank b, Response response) {
        if (trans.getFrom().equals("bank")) {
            if (bezahlen(trans, b, trans.getTo(), response, trans.getAmount())){
                b.getTransactionList().add(trans);
            }
        } else if (trans.getTo().equals("bank")) {
            if (bezahlen(trans, b, trans.getFrom(), response,trans.getAmount().negate())){
                b.getTransactionList().add(trans);
            }
        } else if (!(trans.getTo().equals("bank")) && !(trans.getFrom().equals("bank"))) {
            if (bezahlen(trans, b, trans.getTo(), response, trans.getAmount())){
               if( bezahlen(trans, b, trans.getFrom(), response, trans.getAmount().negate())){
                    b.getTransactionList().add(trans);
               }else {
                   //bei playerFrom wieder den Guthaben aufaddieren
                   bezahlen(trans, b, trans.getTo(), response, trans.getAmount().negate());
               }
            }else{
                response.status(403);
            }

        } else {
            response.status(403);
        }
    }

    public boolean bezahlen(Transaction trans, Bank b, String playerId, Response response, BigDecimal amount) {
        for (Account a : b.getAccountList()) {
            if (a.getPlayer().getPlayerId().equals(playerId)) {
                if (a.getSaldo().compareTo(amount.abs()) < 0) {
                    response.status(403);
                    return false;
                }
                a.setSaldo(a.getSaldo().add(amount));
                return  true;
            }
        }
        return false;
    }

    //Player existiert schon, nur sein Account noch nicht
    public Object createAccount(Request request, Response response) {
        String id = request.params(":gameId");
        Player player;
        Account account = null;
        Bank bank = null;
        try {
            account = gson.fromJson(request.body(), Account.class);
        } catch (JsonSyntaxException e) {
            return "json exception";
        }
        player = account.getPlayer();
        if (player != null) {
            for (Bank b : bankList) {
                if (b.getBankId().equals(id)) {
                    bank = b;
                    for (Account a : b.getAccountList())
                        if (a.getPlayer().equals(player)) {
                            response.status(409);
                            return "";
                        }
                    bank.addAccount(account);
                    response.status(201);
                    return "account wurde erstellt";
                }
            }
        }
        response.status(404);
        return "player is null";
    }

}
