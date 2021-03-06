package gec.data.file;

import gec.core.ConsoleEnum;
import gec.data.GameMetaData;
import gec.data.image.ImageHandler;
import gec.data.rom.crawler.RomInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileHandlerImpl implements FileHandler {
    private static final Logger log = LoggerFactory.getLogger(FileHandlerImpl.class);
    public static final String ROOT_FOLDER_NAME = "GEC";
    @Value("${custom.root.location}")
    private String rootPath;
    private List<String> consoleList;

    @PostConstruct
        // Package visible for test purpose
    void initFileStructure() {
        consoleList = Stream.of(ConsoleEnum.values())
                .map(ConsoleEnum::getConsoleName)
                .collect(Collectors.toList());

        createFolderStructure();
        copyCacheToRoot();
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public List<String> getConsoleList() {
        return consoleList;
    }

    @Override
    public boolean fileExists(String absoluteFilePath) {
        return new File(absoluteFilePath).exists();
    }

    @Override
    // Not perfect but there should at most only be two files in any given folder, previewImage and rom
    public boolean romExists(GameMetaData game) {
        String fullPath = convertToPath(game);
        String imagePath = fullPath + ImageHandler.IMAGE_NAME;
        File romFolder = new File(fullPath);
        return Arrays.stream(romFolder.listFiles()).map(File::getAbsolutePath)
                .map(string -> string.replace("\\", "/"))
                .anyMatch(Predicate.not(imagePath::equals));
    }

    @Override
    public List<String> readLinesFromFile(String filePath) {
        URL url = this.getClass().getClassLoader().getResource(filePath);
        List<String> list = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(url.toURI()));
            while (s.hasNextLine()) {
                String row = s.nextLine();
                // Ignore comment rows
                if (row.charAt(0) == '#') {
                    continue;
                }
                list.add(row);
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Publish new type of error event
        }

        return list;
    }

    @Override
    public void saveImageToDisk(BufferedImage image, GameMetaData game) {
        String fullPath = convertToPath(game);
        String imagePath = fullPath + ImageHandler.IMAGE_NAME;

        File file = new File(fullPath);
        file.mkdirs();

        try {
            ImageIO.write(image, "png", new File(imagePath));
        } catch (IOException e) {
            log.error("Error while creating image '{}'", imagePath);
            e.printStackTrace();
            // TODO Publish new type of error event
            throw new RuntimeException();
        }
    }

    @Override
    public void saveRomToDisk(RomInfo romInfo, GameMetaData game) {
        String fullPath = convertToPath(game);
        String romPath = fullPath + romInfo.getFileName();

        try {
            OutputStream outputStream = new FileOutputStream(romPath);
            outputStream.write(romInfo.getInputStream().readAllBytes());
            outputStream.close();
        } catch (IOException e) {
            log.error("Error while creating rom '{}'", romPath);
            e.printStackTrace();
            // TODO Publish new type of error event
            throw new RuntimeException();
        }
    }

    private void createFolderStructure() {
        // Create root
        rootPath += rootPath.isEmpty() ? Path.of("").toAbsolutePath() : ROOT_FOLDER_NAME;
        createFolder(rootPath);

        // Create console folders
        for (String console : consoleList) {
            createFolder(rootPath + "/" + console);
        }
    }

    private void createFolder(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            log.debug("Creating folder at '{}'", path);
            dir.mkdirs();
        }
    }

    private void copyCacheToRoot() {
        File cacheFolder = new File(getClass().getClassLoader().getResource("precompiledCaches").getPath());
        File rootFolder = new File(getRootPath());
        try {
            FileUtils.copyDirectory(cacheFolder, rootFolder);
        } catch (IOException e) {
            // TODO Nice error to ui? Return gracefully?
            log.error("Could not copy caches from resources to root folder!");
            e.printStackTrace();
        }
    }

    private String convertToPath(GameMetaData game) {
        // Remove illegal characters from game title
        game.setGameTitle(game.getGameTitle().replaceAll("[^a-zA-Z0-9.\\- ]", ""));
        String fullPath = rootPath + "/" + game.getConsole().getConsoleName() + "/" + game.getGameTitle() + "/";

        File file = new File(fullPath);
        file.mkdirs();

        return fullPath;
    }
}
