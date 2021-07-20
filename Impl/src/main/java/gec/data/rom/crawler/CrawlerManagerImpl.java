package gec.data.rom.crawler;

import gec.core.ConsoleEnum;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.AbstractSite;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class CrawlerManagerImpl implements CrawlerManager {
    @Autowired
    FileHandler fileHandler;
    @Autowired
    RomsKingdomDotCom romsKingdomDotCom;

    @Override
    public List<String> findLinks(ConsoleEnum console, String gameTitle) {
        return null;
    }

    @Override
    public boolean downloadRom(SupportedSiteEnum site, String url) {
        try {
            InputStream is = getSiteComponent(site).downloadRom(url);
            return fileHandler.saveFile(is, "rom.nnn"); // TODO Find file type (yes) and name (maybe) // TODO Solve filePath
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Nice error to UI?
        }
        return false;
    }

    private AbstractSite getSiteComponent(SupportedSiteEnum site) {
        return switch (site) {
            case ROMS_KINGDOM_DOT_COM -> romsKingdomDotCom;
            default -> throw new RuntimeException("Unsupported site!");

            // TODO Nice error to UI?
        };
    }
}
