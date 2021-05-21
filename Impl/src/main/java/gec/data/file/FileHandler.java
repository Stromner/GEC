package gec.data.file;

import java.util.List;

public interface FileHandler {
    void initFileStructure();

    String getRootPath();

    List<String> getConsoleList();

    boolean fileExists(String absoluteFilePath);

    List<String> readLinesFromFile(String filePath);
}
