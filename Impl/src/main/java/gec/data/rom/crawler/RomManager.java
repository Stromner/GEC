package gec.data.rom.crawler;

import gec.data.GameMetaData;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface RomManager {
    List<Pair<SupportedSiteEnum, String>> findUrls(GameMetaData game);

    void downloadRom(GameMetaData game, SupportedSiteEnum site, String url);
}
