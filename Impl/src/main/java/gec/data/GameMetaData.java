package gec.data;

import gec.core.ConsoleEnum;

public class GameMetaData {
    private String gameTitle;
    private ConsoleEnum console;

    public GameMetaData(String gameTitle, ConsoleEnum console) {
        this.gameTitle = gameTitle;
        this.console = console;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public ConsoleEnum getConsole() {
        return console;
    }

    public void setConsole(ConsoleEnum console) {
        this.console = console;
    }

    @Override
    public String toString() {
        return "GameMetaData{" +
                "gameTitle='" + gameTitle + '\'' +
                ", console=" + console +
                '}';
    }
}
