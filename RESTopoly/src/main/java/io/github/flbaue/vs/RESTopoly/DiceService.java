package io.github.flbaue.vs.RESTopoly;

import com.google.gson.Gson;

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
        get("/dice", (request, response) -> {
            Roll roll = new Roll(new Random().nextInt(6) + 1);

            String json = gson.toJson(roll);
            return json;
        });
    }
}
