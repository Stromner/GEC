package gec.data.rom.crawler.sites;

import gec.core.ConsoleEnum;
import gec.data.rom.crawler.RomInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TestRomsKingdomDotCom {
    private String DIRECT_LINK =
            "https://romskingdom.com/en/download-roms/snes-super-nintendo/super-mario-world-usa/start";
    @InjectMocks
    private RomsKingdomDotCom unitUnderTest;

    @Test
    public void testFindRomUrl() throws IOException {
        String url = unitUnderTest.findUrl(ConsoleEnum.SNES, "Super Mario World");
        assertEquals(url, DIRECT_LINK);
    }

    @Test
    public void testDownloadRomDirectLink() throws IOException {
        RomInfo romInfo = unitUnderTest.downloadRom(DIRECT_LINK);
        assertTrue(romInfo.getInputStream().readAllBytes().length > 0);
        assertEquals("Super Mario World (USA).zip", romInfo.getFileName());
    }
}
