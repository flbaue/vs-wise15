package io.github.flbaue.sparkdemo;

import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florian on 22.10.15.
 */
public class MainController extends Controller {

    private List<String> items = new ArrayList<>();

    public MainController() {
        get("/hello/:name", this::helloName);
        post("/list", this::createListItem);
    }

    public Object helloName(Request request, Response response) {
        return "Hello " + request.params(":name");
    }

    public Object createListItem(Request request, Response response) {
        items.add(request.body());
        return items;
    }
}
