package gec.data.rom.crawler;

import java.io.InputStream;

public class RomInfo {
    private InputStream is;
    private String fileName;

    public RomInfo() {
    }

    public RomInfo(String fileName, InputStream is) {
        this.fileName = fileName;
        this.is = is;
    }

    public InputStream getInputStream() {
        return is;
    }

    public void setInputStream(InputStream is) {
        this.is = is;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
