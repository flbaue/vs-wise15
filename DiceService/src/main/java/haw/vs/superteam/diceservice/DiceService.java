package haw.vs.superteam.diceservice;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Random;

import static spark.Spark.get;

/**
 * Created by florian on 24.10.15.
 */
public class DiceService {

    private Gson gson = new Gson();

    public static void main(String[] args) {
        new DiceService().run();
    }

    private void run() {
        System.out.println("Dice Service is starting");

        get("/", this::root);
        get("/dice", this::rollDice);

        registerService();
    }

    private void registerService() {
      try {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String uri = "https://vs-docker.informatik.haw-hamburg.de/cnt/" + ip + "/4567";

        OkHttpClient client = Utils.getUnsafeOkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse(
                "application/json"),
                "{" +
                        "\"name\": \"dice\", " +
                        "\"description\": \"DiceService of team superteam\", " +
                        "\"service\": \"dice\", " +
                        "\"uri\": \"" + uri + "\"" +
                        "}");

        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .post(body)
                .url("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")
                .build();


            com.squareup.okhttp.Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                System.err.print("DiceService is registered");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object root(Request request, Response response) throws Exception {
        return "DiceService of team superteam";
    }

    private Object rollDice(Request request, Response response) throws Exception {

        Roll roll = new Roll(new Random().nextInt(6) + 1);
        String json = gson.toJson(roll);
        return json;
    }
}
