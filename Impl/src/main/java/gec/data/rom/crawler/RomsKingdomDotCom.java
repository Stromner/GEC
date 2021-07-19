package gec.data.rom.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

// POC that you can trigger a file download programmatically
@Component
public class RomsKingdomDotCom {
    private InputStream is;

    public InputStream downloadRom(String url) throws IOException {
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);

        webClient.setAttachmentHandler((AttachmentHandler) page -> {
            try {
                is = page.getWebResponse().getContentAsStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10000);

        return is;
    }
}
