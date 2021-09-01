package gec.data.rom.crawler.sites;

import gec.core.ConsoleEnum;
import gec.data.rom.crawler.RomInfo;

import java.io.IOException;

public interface RomPage {
    void initCache();

    void clearCache();

    String findRomUrl(ConsoleEnum console, String gameTitle) throws IOException, InterruptedException;

    RomInfo downloadRom(String url) throws IOException;
}
