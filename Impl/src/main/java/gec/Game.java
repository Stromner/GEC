package gec;

public class Game {
    private String gameTitle;
    private ConsoleEnum console;

    public Game(String gameTitle, ConsoleEnum console) {
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
}
