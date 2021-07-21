package gec.data.rom.crawler;

import gec.core.ConsoleEnum;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.AbstractSite;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CrawlerManagerImpl implements CrawlerManager {
    @Autowired
    private FileHandler fileHandler;
    @Autowired
    private RomsKingdomDotCom romsKingdomDotCom;

    @Override
    public List<String> findLinks(ConsoleEnum console, String gameTitle) {
        return null;
    }

    @Override
    public void downloadRom(ConsoleEnum console, String gameTitle, SupportedSiteEnum site, String url) {
        try {
            RomInfo romInfo = getSiteComponent(site).downloadRom(url);
            fileHandler.saveFile(romInfo, console, gameTitle);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Nice error to UI?
        }
    }

    private AbstractSite getSiteComponent(SupportedSiteEnum site) {
        return switch (site) {
            case ROMS_KINGDOM_DOT_COM -> romsKingdomDotCom;
            default -> throw new RuntimeException("Unsupported site!");

            // TODO Nice error to UI?
        };
    }
}
