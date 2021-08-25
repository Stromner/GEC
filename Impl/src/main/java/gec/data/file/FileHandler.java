package gec.data.file;

import gec.data.GameMetaData;
import gec.data.rom.crawler.RomInfo;

import java.awt.image.BufferedImage;
import java.util.List;

public interface FileHandler {
    void initFileStructure();

    String getRootPath();

    List<String> getConsoleList();

    boolean fileExists(String absoluteFilePath);

    List<String> readLinesFromFile(String filePath);

    void saveImageToDisk(BufferedImage image, GameMetaData gameMetaData);

    void saveRomToDisk(RomInfo romInfo, GameMetaData gameMetaData);
}
