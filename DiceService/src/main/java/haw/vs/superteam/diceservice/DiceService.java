package haw.vs.superteam.diceservice;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

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
    }

    private Object root(Request request, Response response) throws Exception {
        return "It's Alive!";
    }

    private Object rollDice(Request request, Response response) throws Exception {

        Roll roll = new Roll(new Random().nextInt(6) + 1);
        String json = gson.toJson(roll);
        return json;
    }
}
