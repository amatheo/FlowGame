package io.github.amatheo.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public enum Level {
    LEVEL1("level_1"),
    LEVEL2("level_2"),
    LEVEL3("level_3"),
    LEVEL4("level_3"),
    LEVEL5("level_3");

    public String path = null;
    Level(String filename) {
        try {
            URI uri = ClassLoader.getSystemResource("levels/"+filename+".json").toURI();
            path = Paths.get(uri).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
