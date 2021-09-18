package gec.data.rom.crawler;

import gec.data.GameMetaData;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.RomPage;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    private void init() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (RomPage.class.isAssignableFrom(f.getType())) {
                try {
                    RomPage site = (RomPage) f.get(this);
                    site.initCache();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // TODO Nice error to UI?
                }
            }
        }
    }

    @Override
    public List<Pair<SupportedSiteEnum, String>> findUrls(GameMetaData game) {
        List<CompletableFuture<Pair<SupportedSiteEnum, String>>> threads = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (RomPage.class.isAssignableFrom(f.getType())) {
                threads.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        RomPage site = (RomPage) f.get(this);
                        return new ImmutablePair<>(site.getSiteEnum(),
                                site.findRomUrl(game.getConsole(), game.getGameTitle()));
                    } catch (IOException | InterruptedException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Could not get link!");
                        // TODO Nice error to UI? Ensure it crashes nicely (program can continue)?
                    }
                }));
            }
        }

        return threads.stream().map(CompletableFuture::join).filter(pair -> !pair.getValue().isBlank())
                .collect(Collectors.toList());
    }

    @Override
    public void downloadRom(GameMetaData game, SupportedSiteEnum site, String url) {
        if (fileHandler.romExists(game)) {
            log.debug("rom already exists, returning...");
            return;
        }
        try {
            RomInfo romInfo = getSiteComponent(site).downloadRom(url);
            fileHandler.saveRomToDisk(romInfo, game);
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
