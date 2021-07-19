package gec.data.rom.crawler;

public enum SupportedSites {
    ROMS_KINGDOM_DOT_COM("romskingdom.com");

    private String url;

    SupportedSites(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
