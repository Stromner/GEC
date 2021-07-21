package gec.data.rom.crawler;

import gec.core.ConsoleEnum;

import java.util.List;

public interface CrawlerManager {
    List<String> findLinks(ConsoleEnum console, String gameTitle);

    void downloadRom(ConsoleEnum console, String gameTitle, SupportedSiteEnum site, String url);
}
