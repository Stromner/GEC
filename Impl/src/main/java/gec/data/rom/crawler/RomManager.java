package gec.data.rom.crawler;

import gec.core.ConsoleEnum;

import java.util.List;

public interface RomManager {
    List<String> findUrls(ConsoleEnum console, String gameTitle) throws IllegalAccessException;

    void downloadRom(ConsoleEnum console, String gameTitle, SupportedSiteEnum site, String url);
}
