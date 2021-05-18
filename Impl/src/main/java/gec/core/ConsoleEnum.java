package gec.core;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ConsoleEnum {
    PLAYSTATION_ONE("Playstation 1"),
    NINTENDO_64("Nintendo 64"),
    SNES("SNES");

    private String consoleName;
    private static final Map<String,ConsoleEnum> ENUM_MAP;

    static {
        Map<String,ConsoleEnum> map = new ConcurrentHashMap<>();
        for (ConsoleEnum instance : ConsoleEnum.values()) {
            map.put(instance.getConsoleName().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    ConsoleEnum(String consoleName) {
        this.consoleName = consoleName;
    }

    public String getConsoleName() {
        return consoleName;
    }

    public static ConsoleEnum get (String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
