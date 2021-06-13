package gec.data.console;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import gec.data.file.FileHandler;
import gec.data.image.ImageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ConsoleHandlerImpl implements ConsoleHandler {
    private static final Logger log = LoggerFactory.getLogger(ConsoleHandlerImpl.class);
    @Autowired
    ImageHandler imageHandler;
    @Autowired
    FileHandler fileHandler;
    private String selectedConsole;
    private List<String> gameList;

    @Override
    public void selectConsole(String selectedConsole) {
        this.selectedConsole = selectedConsole;
        String gameListPath = selectedConsole + ".txt";
        gameList = fileHandler.readLinesFromFile(gameListPath);
    }

    @Override
    public List<String> getGameList() {
        return gameList;
    }

    @Override
    public BufferedImage getGamePreviewImage(String gameTitle) {
        // Remove illegal characters from game title
        gameTitle = gameTitle.replaceAll("[^a-zA-Z0-9.\\-]", "");

        String absoluteFilePath = fileHandler.getRootPath() + "/" + selectedConsole + "/" + gameTitle;
        absoluteFilePath += "/" + ImageHandler.IMAGE_NAME;

        try {
            if (!fileHandler.fileExists(absoluteFilePath)) {
                return downloadImage(gameTitle, absoluteFilePath);
            } else {
                return ImageIO.read(new File(absoluteFilePath));
            }
        } catch (IOException e) {
            log.error("Error while creating image '{}'", absoluteFilePath);
            e.printStackTrace();
            // TODO Publish new type of error event
            throw new RuntimeException();
        }
    }

    private BufferedImage downloadImage(String gameTitle, String absoluteFilePath) throws IOException {
        GameMetaData game = new GameMetaData(gameTitle, ConsoleEnum.get(selectedConsole));
        BufferedImage image = imageHandler.downloadImage(game);

        // Create image locally
        File file = new File(absoluteFilePath);
        file.mkdirs();
        ImageIO.write(image, "png", new File(absoluteFilePath));

        return image;
    }
}
