package gec.data.console;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ConsoleHandler {
    void selectConsole(String selectedConsole);

    List<String> getGameList();

    BufferedImage getGamePreviewImage(String game);
}
