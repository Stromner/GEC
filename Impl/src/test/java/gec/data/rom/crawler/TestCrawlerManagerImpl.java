package gec.data.rom.crawler;

import gec.core.ConsoleEnum;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.sites.RomsKingdomDotCom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestCrawlerManagerImpl {
    @Mock
    FileHandler fileHandler;
    @Mock
    RomsKingdomDotCom romsKingdomDotCom;
    @InjectMocks
    CrawlerManagerImpl unitUnderTest;

    @Test
    public void testFindUrls() throws IllegalAccessException, IOException, InterruptedException {
        String url = "testUrl";
        when(romsKingdomDotCom.findRomUrl(any(), anyString())).thenReturn(url);

        List<String> foundUrls = unitUnderTest.findUrls(ConsoleEnum.NINTENDO_64, "gameTitle");
        assertTrue(foundUrls.contains(url));
    }

    @Test
    public void testNoUrlsFound() throws IllegalAccessException, IOException, InterruptedException {
        when(romsKingdomDotCom.findRomUrl(any(), anyString())).thenReturn("");

        List<String> foundUrls = unitUnderTest.findUrls(ConsoleEnum.NINTENDO_64, "gameTitle");
        assertTrue(foundUrls.isEmpty());
    }

    @Test
    public void testDownloadSuccess() throws IOException {
        when(romsKingdomDotCom.downloadRom(anyString())).thenReturn(any(RomInfo.class));

        unitUnderTest.downloadRom(ConsoleEnum.NINTENDO_64, "gameTitle", SupportedSiteEnum.ROMS_KINGDOM_DOT_COM, "url");
    }

    @Test
    public void testDownloadFailed() throws IOException {
        when(romsKingdomDotCom.downloadRom(anyString())).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () ->
                unitUnderTest.downloadRom(ConsoleEnum.NINTENDO_64, "gameTitle", SupportedSiteEnum.ROMS_KINGDOM_DOT_COM,
                        "url"));
    }
}
