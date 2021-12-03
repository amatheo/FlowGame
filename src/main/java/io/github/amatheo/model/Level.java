package io.github.amatheo.model;

import java.net.URISyntaxException;
import java.net.URL;

public enum Level {
    LEVEL1("level_1"),
    LEVEL2("level_2"),
    LEVEL3("level_3"),
    LEVEL4("level_3"),
    LEVEL5("level_3");

    public String path = null;
    Level(String path) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource("levels/"+path+".json");
        try {
            this.path = url.toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
