package io.github.flbaue.sparkdemo;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.*;

/**
 * Created by florian on 22.10.15.
 */
public abstract class Controller implements Route {

    private Map<Path, Handler> pathMap = new TreeMap<>();


    public void addRoute(Verb verb, String path, Handler handler) {
        Objects.requireNonNull(verb, "Verb must not be null");
        Objects.requireNonNull(path, "Path must not be null");
        Objects.requireNonNull(handler, "Handler must not be null");

        Path p = new Path(path);

        if (pathMap.containsKey(p)) {
            throw new IllegalArgumentException("Path already defined: " + path);
        }

        pathMap.put(p, handler);

        switch (verb) {
            case GET:
                Spark.get(p.string, this);
                break;
            case POST:
                Spark.get(p.string, this);
                break;
            case PUT:
                Spark.get(p.string, this);
                break;
            case DELETE:
                Spark.get(p.string, this);
                break;
        }
    }

    public void get(String path, Handler handler) {
        addRoute(Verb.GET, path, handler);
    }

    public void post(String path, Handler handler) {
        addRoute(Verb.POST, path, handler);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Path requestPath = new Path(request.pathInfo());
        Handler m = pathMap.get(requestPath);
        return m.handle(request, response);
    }

    public interface Handler {
        Object handle(Request request, Response response);
    }

    public enum Verb {
        GET, POST, PUT, DELETE;
    }
}
