package gec.data.image;

import gec.data.ConsoleEnum;
import gec.data.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void beforeEach() {
        file = new File(IMAGE_NAME);
    }

    @AfterEach
    public void afterEach() {
        cleanUpCreatedFile();
    }

    @Test
    public void testFindImageUrlBing_WithoutAds() {
        Game game = new Game("Digimon World 1", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(game);
        assertTrue(file.exists());
    }

    @Test
    public void testFindImageUrlBing_WithAds() {
        Game game = new Game("Final Fantasy 7", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(game);
        assertTrue(file.exists());
    }

    private void cleanUpCreatedFile() {
        assertTrue(file.delete());
    }
}
