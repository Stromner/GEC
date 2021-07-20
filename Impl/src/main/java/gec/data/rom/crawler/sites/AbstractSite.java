package gec.data.rom.crawler.sites;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public abstract class AbstractSite {
    public abstract InputStream downloadRom(String url) throws IOException;
}
