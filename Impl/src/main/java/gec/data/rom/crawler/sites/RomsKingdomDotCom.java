package gec.data.rom.crawler.sites;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import gec.core.ConsoleEnum;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.RomInfo;
import info.debatty.java.stringsimilarity.RatcliffObershelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RomsKingdomDotCom extends AbstractSite implements RomPage {
    private static final String CACHE_NAME = "RomsKingdom.com";
    private final String site = "https://romskingdom.com/";
    private String cachePath;
    @Autowired
    private FileHandler fileHandler;

    @Override
    public void initCache() {
        cachePath = fileHandler.getRootPath() + "/RomsKingdomDotCom";
        initCache(cachePath);
    }

    @Override
    public void clearCache() {
        diskCache.remove(CACHE_NAME);
        diskCache.close();
    }

    @Override
    public String findRomUrl(ConsoleEnum console, String gameTitle) throws IOException, InterruptedException {
        List<String> hrefs = getHrefsForSite(site, CACHE_NAME);
        String searchCrumbs = getEmulatorNameString(console) + gameTitle + "/start";

        Double bestScore = null;
        String bestScoreHref = "";
        for (String href : hrefs) {
            var distanceAlgorithm = new RatcliffObershelp();
            double localScore = distanceAlgorithm.distance(href, searchCrumbs);
            if (bestScore == null || localScore < bestScore) {
                bestScoreHref = href;
                bestScore = localScore;
            }
        }

        return bestScoreHref;
    }

    @Override
    public RomInfo downloadRom(String url) throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);

        RomInfo romInfo = new RomInfo();

        webClient.setAttachmentHandler((AttachmentHandler) page -> {
            try {
                romInfo.setInputStream(page.getWebResponse().getContentAsStream());
                romInfo.setFileName(extractFileName(page.getWebResponse().getResponseHeaders()));
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Nice error to UI?
            }
        });

        webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10000);

        return romInfo;
    }

    private String extractFileName(List<NameValuePair> responseHeaders) {
        // TODO Handle error gracefully if name can't be found?
        //  (Need to handle two cases: NameValuePair missing and NameValuePair found but regex fails)
        for (NameValuePair item : responseHeaders) {
            if (item.getName().equalsIgnoreCase("Content-Disposition")) {
                Pattern pattern = Pattern.compile("\"(.*)\"", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(item.getValue());
                if (matcher.find()) {
                    return matcher.group(1);
                } else {
                    throw new RuntimeException("Regex failed for following header value: " + item.getValue());
                }
            }
        }
        throw new RuntimeException("Missing header that contains fileName!");
    }

    private String getEmulatorNameString(ConsoleEnum console) {
        return switch (console) {
            case PLAYSTATION_ONE -> "ps1-psx-playstation";
            case NINTENDO_64 -> "n64-nintendo-64";
            case SNES -> "snes-super-nintendo";
            default ->
                    // TODO Might want to make it checked so it doesn't crashes the program
                    throw new RuntimeException("Unsupported console for site!");
        };
    }
}
