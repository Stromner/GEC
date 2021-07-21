package gec.data.file;

import gec.core.ConsoleEnum;
import gec.data.rom.crawler.RomInfo;

import java.util.List;

public interface FileHandler {
    void initFileStructure();

    String getRootPath();

    List<String> getConsoleList();

    boolean fileExists(String absoluteFilePath);

    List<String> readLinesFromFile(String filePath);

    void saveFile(RomInfo romInfo, ConsoleEnum console, String gameTitle);
}
