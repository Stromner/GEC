package gec;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
            e.printStackTrace();
            throw new ImageException("Failed to get page!", e.getCause());
        }
    }

    // TODO This does not work for if the page has ads
    private String getImageUrl(Document document) {
        Elements matchingElements = document.select("[data-idx=1] .mimg");
        Element element = matchingElements.get(0);

        UrlDetector parser = new UrlDetector(element.toString(), UrlDetectorOptions.Default);
        List<Url> found = parser.detect();
        if (found.size() < 1) {
            log.error("Could not find any image to download!");
            throw new ImageException("Could not find any image to download!");
        }

        String imageUrl = found.get(0).getHost() + found.get(0).getPath();
        log.debug("Image URL '{}'", imageUrl);

        return imageUrl;
    }
}
