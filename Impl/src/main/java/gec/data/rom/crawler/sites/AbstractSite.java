package gec.data.rom.crawler.sites;

import gec.core.ConsoleEnum;
import gec.data.rom.crawler.RomInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public abstract class AbstractSite {

    public abstract String findUrl(ConsoleEnum console, String gameTitle) throws IOException;

    public abstract RomInfo downloadRom(String url) throws IOException;
}
