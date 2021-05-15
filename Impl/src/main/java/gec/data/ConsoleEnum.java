package gec.data;

public enum ConsoleEnum {
    PLAYSTATION_ONE("Playstation 1");

    private String consoleName;

    ConsoleEnum(String consoleName) {
        this.consoleName = consoleName;
    }

    public String getConsoleName() {
        return consoleName;
    }
}
