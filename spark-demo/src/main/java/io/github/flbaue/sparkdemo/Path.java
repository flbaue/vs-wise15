package io.github.flbaue.sparkdemo;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by florian on 22.10.15.
 */
public final class Path implements Comparable<Path> {

    private final String[] pathArray;
    public final String string;

    public Path(String path) {
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }

        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 2);
        }

        this.pathArray = path.split("/");
        this.string = path;
    }


    @Override
    public String toString() {
        return "Path{" +
                "string='" + string + '\'' +
                '}';
    }

    @Override
    public int compareTo(Path o) {
        Objects.requireNonNull(o, "Cannot compare path to null");

        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i].charAt(0) == ':' && o.pathArray[i].charAt(0) != ':'
                    || pathArray[i].charAt(0) != ':' && o.pathArray[i].charAt(0) == ':') {
                continue;
            }

            int result = pathArray[i].compareTo(o.pathArray[i]);
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }
}
