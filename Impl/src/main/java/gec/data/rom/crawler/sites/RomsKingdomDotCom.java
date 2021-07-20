package gec.data.rom.crawler.sites;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RomsKingdomDotCom extends AbstractSite {
    private InputStream is;

    @Override
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
