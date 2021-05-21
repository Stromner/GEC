package gec.data.console;

import gec.core.FileHandler;
import gec.data.image.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.List;

@Component
public class ConsoleHandlerImpl implements ConsoleHandler {
    @Autowired
    ImageHandler imageHandler;
    private String selectedConsole;
    private List<String> gameList;

    @Override
    public void selectConsole(String selectedConsole) {
        this.selectedConsole = selectedConsole;
        String gameListPath = selectedConsole + ".txt";
        gameList = FileHandler.readLinesFromFile(gameListPath);
    }

    @Override
    public List<String> getGameList() {
        return gameList;
    }

    @Override
    public BufferedImage getGamePreviewImage(String game) {
        return null;
    }
}
