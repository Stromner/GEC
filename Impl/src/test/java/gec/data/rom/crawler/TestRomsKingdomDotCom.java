package gec.data.rom.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TestRomsKingdomDotCom {
    @InjectMocks
    private RomsKingdomDotCom unitUnderTest;

    @Test
    public void testDownloadRomDirectLink() throws IOException {
        InputStream is = unitUnderTest.downloadRom("https://romskingdom.com/en/download-roms/snes-super-nintendo/super-mario-world-usa/start");
        assertTrue(is.readAllBytes().length > 0);
    }
}
