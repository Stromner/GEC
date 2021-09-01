package gec.data.rom.crawler.sites;

import gec.core.ConsoleEnum;
import gec.data.file.FileHandler;
import gec.data.rom.crawler.RomInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestRomsKingdomDotCom {
    private static final Logger log = LoggerFactory.getLogger(TestRomsKingdomDotCom.class);
    private final String DIRECT_LINK =
            "https://romskingdom.com/en/download-roms/snes-super-nintendo/super-mario-world-usa/start";
    private String TEST_CACHE_PATH = System.getProperty("user.dir") + "/src/main/resources/precompiledCaches";
    @Mock
    private FileHandler fileHandler;
    @InjectMocks
    private RomsKingdomDotCom unitUnderTest;

    @Test
    public void testMissingInitCache() {
        assertThrows(RuntimeException.class, () -> unitUnderTest.findRomUrl(ConsoleEnum.NINTENDO_64, "gameTitle"));
    }

    @Test
    public void testCacheSite() throws IOException, InterruptedException, URISyntaxException {
        // SETUP
        when(fileHandler.getRootPath()).thenReturn(TEST_CACHE_PATH);
        unitUnderTest.initCache();

        var cachePath = Paths.get(getClass().getClassLoader().getResource("").toURI());
        if (Files.exists(cachePath)) {
            log.warn("Cache already exist in resources, skipping...");
        } else {
            unitUnderTest.findRomUrl(ConsoleEnum.SNES, "");
            assertTrue(Files.exists(cachePath));
        }
    }

    @Test
    public void testFindRomUrl() throws IOException, InterruptedException {
        when(fileHandler.getRootPath()).thenReturn(TEST_CACHE_PATH);
        unitUnderTest.initCache();

        String url = unitUnderTest.findRomUrl(ConsoleEnum.SNES, "Super Mario World");
        assertEquals(DIRECT_LINK, url);
    }

    @Test
    public void testDownloadRomDirectLink() throws IOException {
        RomInfo romInfo = unitUnderTest.downloadRom(DIRECT_LINK);
        assertTrue(romInfo.getInputStream().readAllBytes().length > 0);
        assertEquals("Super Mario World (USA).zip", romInfo.getFileName());
    }
}
