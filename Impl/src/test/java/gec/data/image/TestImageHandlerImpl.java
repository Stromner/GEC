package gec.data.image;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TestImageHandlerImpl {
    @InjectMocks
    private ImageHandlerImpl unitUnderTest;

    @Test
    public void testFindImageUrlBing_WithoutAds() {
        GameMetaData gameMetaData = new GameMetaData("Digimon World 1", ConsoleEnum.PLAYSTATION_ONE);
        BufferedImage image = unitUnderTest.downloadImage(gameMetaData);
        assertNotNull(image);
    }

    @Test
    public void testFindImageUrlBing_WithAds() {
        GameMetaData gameMetaData = new GameMetaData("Final Fantasy 7", ConsoleEnum.PLAYSTATION_ONE);
        BufferedImage image = unitUnderTest.downloadImage(gameMetaData);
        assertNotNull(image);
    }

    @Test
    public void testLoadImageFromDisk_ImageMissing() {
        assertThrows(RuntimeException.class, () -> unitUnderTest.loadImageFromDisk(""));
    }
}
