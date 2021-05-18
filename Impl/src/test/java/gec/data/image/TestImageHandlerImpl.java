package gec.data.image;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static gec.data.image.ImageHandler.IMAGE_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TestImageHandlerImpl {
    private File file;

    @InjectMocks
    private ImageHandlerImpl unitUnderTest;

    @AfterEach
    public void afterEach() {
        cleanUpCreatedFile();
    }

    @Test
    public void testFindImageUrlBing_WithoutAds() {
        GameMetaData gameMetaData = new GameMetaData("Digimon World 1", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(gameMetaData);
        assertTrue(file.exists());
    }

    @Test
    public void testFindImageUrlBing_WithAds() {
        GameMetaData gameMetaData = new GameMetaData("Final Fantasy 7", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(gameMetaData);
        assertTrue(file.exists());
    }

    private void cleanUpCreatedFile() {
        file = new File(IMAGE_NAME);
        assertTrue(file.delete());
    }
}
