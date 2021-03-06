package gec.data.image;

import gec.core.exceptions.ImageException;
import gec.data.GameMetaData;
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
    public BufferedImage downloadImage(GameMetaData gameMetaData) {
        String imageUrl = getImageUrl(getDataFromBing(gameMetaData));

        try {
            URL url = new URL(imageUrl);
            return ImageIO.read(url);
        } catch (IOException e) {
            log.error("Failed to download image '{}'", imageUrl);
            throw new ImageException("Failed to download image!", e);
        }
    }

    @Override
    public BufferedImage loadImageFromDisk(String absoluteFilePath) {
        try {
            return ImageIO.read(new File(absoluteFilePath));
        } catch (IOException e) {
            log.error("Error while creating image '{}'", absoluteFilePath);
            e.printStackTrace();
            // TODO Publish new type of error event
            throw new RuntimeException();
        }
    }

    private Document getDataFromBing(GameMetaData gameMetaData) {
        String connectionString = "https://www.bing.com/images/search?q=";
        connectionString += gameMetaData.getGameTitle();
        connectionString += "+";
        connectionString += gameMetaData.getConsole().getConsoleName();
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

        Pattern patt = Pattern.compile("https?://tse.\\.mm.bing.net.+?(?=&)");
        Matcher matcher = patt.matcher(element.toString());
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            log.error("Could not find any image to download!");
            throw new ImageException("Could not find any image to download!");
        }
    }
}
