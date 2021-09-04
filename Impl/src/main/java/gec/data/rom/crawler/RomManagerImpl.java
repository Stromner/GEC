package gec.data.rom.crawler;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.RomPage;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class RomManagerImpl implements RomManager {
    private static final Logger log = LoggerFactory.getLogger(RomManagerImpl.class);
    @Autowired
    private FileHandler fileHandler;
    @Autowired
    private RomsKingdomDotCom romsKingdomDotCom;

    @Override
    public List<String> findUrls(ConsoleEnum console, String gameTitle) throws IllegalAccessException {
        List<CompletableFuture<String>> threads = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (RomPage.class.isAssignableFrom(f.getType())) {
                RomPage site = (RomPage) f.get(this);
                threads.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        return site.findRomUrl(console, gameTitle);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Could not get link!");
                        // TODO Nice error to UI? Ensure it crashes nicely (program can continue)?
                    }
                }));
            }
        }

        return threads.stream().map(CompletableFuture::join).filter(url -> !url.isBlank()).collect(Collectors.toList());
    }

    @Override
    public void downloadRom(ConsoleEnum console, String gameTitle, SupportedSiteEnum site, String url) {
        GameMetaData metaData = new GameMetaData(gameTitle, console);
        if (fileHandler.romExists(metaData)) {
            log.debug("rom already exists, returning...");
            return;
        }
        try {
            RomInfo romInfo = getSiteComponent(site).downloadRom(url);
            fileHandler.saveRomToDisk(romInfo, metaData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not download rom!");
            // TODO Nice error to UI?
        }
    }

    private RomPage getSiteComponent(SupportedSiteEnum site) {
        return switch (site) {
            case ROMS_KINGDOM_DOT_COM -> romsKingdomDotCom;
            default -> throw new RuntimeException("Unsupported site!");

            // TODO Nice error to UI?
        };
    }
}
