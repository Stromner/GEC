package gec.data.rom.crawler;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestRomManagerImpl {
    @Mock
    FileHandler fileHandler;
    @Mock
    RomsKingdomDotCom romsKingdomDotCom;
    @InjectMocks
    RomManagerImpl unitUnderTest;

    @Test
    public void testFindUrls() throws IOException, InterruptedException {
        String url = "testUrl";
        when(romsKingdomDotCom.findRomUrl(any(), anyString())).thenReturn(url);
        when(romsKingdomDotCom.getSiteEnum()).thenReturn(SupportedSiteEnum.ROMS_KINGDOM_DOT_COM);

        List<Pair<SupportedSiteEnum, String>>
                foundUrls = unitUnderTest.findUrls(new GameMetaData("gameTitle", ConsoleEnum.NINTENDO_64));
        assertTrue(foundUrls.stream().map(Pair::getValue).collect(Collectors.toList()).contains(url));
    }

    @Test
    public void testNoUrlsFound() throws IOException, InterruptedException {
        when(romsKingdomDotCom.findRomUrl(any(), anyString())).thenReturn("");

        List<Pair<SupportedSiteEnum, String>>
                foundUrls = unitUnderTest.findUrls(new GameMetaData("gameTitle", ConsoleEnum.NINTENDO_64));
        assertTrue(foundUrls.isEmpty());
    }

    @Test
    public void testDownloadSuccess() throws IOException {
        when(romsKingdomDotCom.downloadRom(anyString())).thenReturn(any(RomInfo.class));

        unitUnderTest.downloadRom(new GameMetaData("gameTitle", ConsoleEnum.NINTENDO_64),
                SupportedSiteEnum.ROMS_KINGDOM_DOT_COM, "url");
    }

    @Test
    public void testDownloadFailed() throws IOException {
        when(romsKingdomDotCom.downloadRom(anyString())).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () ->
                unitUnderTest.downloadRom(new GameMetaData("gameTitle", ConsoleEnum.NINTENDO_64),
                        SupportedSiteEnum.ROMS_KINGDOM_DOT_COM,
                        "url"));
    }
}
