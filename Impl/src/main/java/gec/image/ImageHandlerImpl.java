package gec.image;

import gec.Game;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageHandlerImpl implements ImageHandler {
    private static final Logger log = LoggerFactory.getLogger(ImageHandlerImpl.class);

    public ImageHandlerImpl() {
    }

    @Override
    public void downloadImage(Game game) {
        String imageUrl = getImageUrl(getDataFromBing(game));

        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            ImageIO.write(image, "png", new File(IMAGE_NAME)); // TODO This needs to go into a unique directory for console+game, would also be useful for tests so files are created in the test structure instead
        } catch (IOException e) {
            log.error("Failed to download image '{}'", imageUrl);
            throw new ImageException("Failed to download image!", e);
        }
    }

    private Document getDataFromBing(Game game) {
        String connectionString = "https://www.bing.com/images/search?q=";
        connectionString += game.getGameTitle();
        connectionString += "+";
        connectionString += game.getConsole().getConsoleName();
        connectionString += "&form=HDRSC2";
        connectionString = connectionString.replace(" ", "+"); // Failsafe if space in Game.java properties

        try {
            return Jsoup.connect(connectionString).get();
        } catch (IOException e) {
            log.error("Failed to get page!");
            throw new ImageException("Failed to get page!", e);
        }
    }

    private String getImageUrl(Document document) {
        String cssQuery = "[data-idx = 1]"; // Find first image
        cssQuery += " ";
        cssQuery += ".iusc"; // Image element

        Elements matchingElements = document.select(cssQuery);
        Element element = matchingElements.get(0);

        Pattern patt = Pattern.compile("https?:\\/\\/tse.\\.mm.bing.net.+?(?=&)");
        Matcher matcher = patt.matcher(element.toString());
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            log.error("Could not find any image to download!");
            throw new ImageException("Could not find any image to download!");
        }
    }
}
