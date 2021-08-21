package gec.data.rom.crawler.sites;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import gec.core.ConsoleEnum;
import gec.data.rom.crawler.RomInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RomsKingdomDotCom extends AbstractSite {
    private final String baseQuery = "https://romskingdom.com/en/download-roms/";
    private final String searchTerm = "?search=";
    private final String suffixSearchTerm = "&order=count_download";

    @Override
    // TODO This is obviously not an elegant way to find the URL, rework it in the future
    public String findUrl(ConsoleEnum console, String gameTitle) throws IOException {
        String searchQuery = baseQuery + getEmulatorNameString(console) + searchTerm + gameTitle + suffixSearchTerm;
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        HtmlPage page = webClient.getPage(searchQuery);
        HtmlAnchor attribute = (HtmlAnchor) page.getByXPath(
                        "//a[contains(@href,'" + baseQuery + getEmulatorNameString(console) + "') and contains(@title, ' ')]")
                .get(0);
        return attribute.getAttribute("href") + "/start";
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
