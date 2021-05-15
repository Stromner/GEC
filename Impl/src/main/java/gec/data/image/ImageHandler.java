package gec.data.image;

import gec.data.GameMetaData;

public interface ImageHandler {
    String IMAGE_NAME = "preview.png";

    void downloadImage(GameMetaData gameMetaData);
}
