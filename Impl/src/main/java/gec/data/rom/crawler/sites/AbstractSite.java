package gec.data.rom.crawler.sites;

import gec.data.rom.crawler.RomInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class AbstractSite {
    public abstract RomInfo downloadRom(String url) throws IOException;
}
