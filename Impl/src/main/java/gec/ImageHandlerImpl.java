package gec;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageHandlerImpl implements ImageHandler {
    private static final Logger log = LoggerFactory.getLogger(ImageHandlerImpl.class);

    public ImageHandlerImpl() {
    }

    @Override
    public void downloadImage(Game game) {
        Document document = getDataFromSite(game);

        String imageUrl = getImageUrl(document); // TODO Download image
    }

    private Document getDataFromSite(Game game) {
        // Bing
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
            throw new ImageException("Failed to get page!", e.getCause());
        }
    }

    private String getImageUrl(Document document) {
        String cssQuery = "[data-idx = 1]"; // Find first image
        cssQuery += " ";
        cssQuery += ".iusc";

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
