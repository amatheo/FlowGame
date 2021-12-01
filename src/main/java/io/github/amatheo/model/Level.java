package io.github.amatheo.model;

import java.net.URL;

public enum Level {
    LEVEL1("level1_0"),
    ;

    public final String path;
    Level(String path) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("levels/"+path+".json");
        this.path = url.getPath();
    }
}
