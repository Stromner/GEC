package gec.data.file;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import gec.data.image.ImageHandler;
import gec.data.rom.crawler.RomInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TestFileHandlerImpl {
    private static final String TEST_PATH = System.getProperty("user.dir") + "/target/" + System.currentTimeMillis();
    private GameMetaData testGame = new GameMetaData("TestGame", ConsoleEnum.NINTENDO_64);
    private RomInfo testRomInfo = new RomInfo("Rom.rom", new ByteArrayInputStream("testStream".getBytes()));

    @InjectMocks
    FileHandlerImpl unitUnderTest;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(unitUnderTest, "rootPath", TEST_PATH);
        MockitoAnnotations.openMocks(this);
        unitUnderTest.initFileStructure();
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.walk(Path.of(unitUnderTest.getRootPath()))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testCreateFileStructure() {
        Path rootPath = Path.of(unitUnderTest.getRootPath());

        // Verify root
        assertTrue(Files.exists(rootPath));

        // Verify each console folder
        for (ConsoleEnum console : ConsoleEnum.values()) {
            Path consolePath = Paths.get(rootPath.toString(), console.getConsoleName());
            assertTrue(Files.exists(consolePath));
        }
    }

    @Test
    public void testSavePreviewImage() {
        unitUnderTest.saveImageToDisk(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), testGame);

        Path imagePath =
                Paths.get(unitUnderTest.getRootPath(), testGame.getConsole().getConsoleName(), testGame.getGameTitle(),
                        ImageHandler.IMAGE_NAME);
        assertTrue(Files.exists(imagePath));
    }

    @Test
    public void testSavePreviewImage_IllegalCharacters() {
        GameMetaData game = new GameMetaData("50x15 - ¿QUIERE SER MILLONARIO?", ConsoleEnum.NINTENDO_64);

        unitUnderTest.saveImageToDisk(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), game);

        Path imagePath = Paths.get(unitUnderTest.getRootPath(), game.getConsole().getConsoleName(), game.getGameTitle(),
                ImageHandler.IMAGE_NAME);
        assertTrue(Files.exists(imagePath));
    }

    @Test
    public void testSaveRom() {
        unitUnderTest.saveRomToDisk(testRomInfo, testGame);

        Path romPath =
                Paths.get(unitUnderTest.getRootPath(), testGame.getConsole().getConsoleName(), testGame.getGameTitle(),
                        testRomInfo.getFileName());
        assertTrue(Files.exists(romPath));
    }

    @Test
    public void testRomExists_NoPreviewImage() {
        unitUnderTest.saveRomToDisk(testRomInfo, testGame);

        assertTrue(unitUnderTest.romExists(testGame));
    }

    @Test
    public void testRomExists_WithPreviewImage() {
        unitUnderTest.saveRomToDisk(testRomInfo, testGame);
        unitUnderTest.saveImageToDisk(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), testGame);

        assertTrue(unitUnderTest.romExists(testGame));
    }

    @Test
    public void testRomExists_EmptyFolder() {
        assertFalse(unitUnderTest.romExists(testGame));
    }
}
