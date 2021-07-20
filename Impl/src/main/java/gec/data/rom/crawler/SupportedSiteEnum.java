package gec.data.rom.crawler;

public enum SupportedSiteEnum {
    ROMS_KINGDOM_DOT_COM("romskingdom.com");

    private String url;

    SupportedSiteEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
