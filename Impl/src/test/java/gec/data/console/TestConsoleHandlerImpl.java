package gec.data.console;

import gec.data.GameMetaData;
import gec.data.file.FileHandlerImpl;
import gec.data.image.ImageHandlerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestConsoleHandlerImpl {
    @Mock
    private FileHandlerImpl fileHandler;
    @Mock
    private ImageHandlerImpl imageHandler;
    @InjectMocks
    private ConsoleHandlerImpl unitUnderTest;
    private String console = "Playstation 1";
    private String rootPath = System.getProperty("user.dir") + "/target/";

    @AfterEach
    public void cleanUp() throws IOException {
        Stream<Path> files = Files.walk(Paths.get(rootPath + console));
        files.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testCreateFile() {
        createFileScenario("TestGame");
    }

    @Test
    public void testCreateFile_IllegalCharacters() {
        createFileScenario("50x15 - ¿QUIERE SER MILLONARIO?");
    }

    @Test
    public void testFileAlreadyExists() {
        // Setup
        String gameTitle = "TestGame2";
        createFileScenario(gameTitle);
        when(fileHandler.fileExists(anyString())).thenReturn(true);

        // Test
        unitUnderTest.getGamePreviewImage(gameTitle);
    }

    private void createFileScenario(String gameTitle) {
        // Mocks
        when(fileHandler.readLinesFromFile(anyString())).thenReturn(new ArrayList<>());
        when(fileHandler.getRootPath()).thenReturn(rootPath);
        when(fileHandler.fileExists(anyString())).thenReturn(false);
        when(imageHandler.downloadImage(any(GameMetaData.class))).thenReturn(new BufferedImage(10, 10, 1));

        // Setup
        unitUnderTest.selectConsole(console);

        // Test
        unitUnderTest.getGamePreviewImage(gameTitle);
    }
}
