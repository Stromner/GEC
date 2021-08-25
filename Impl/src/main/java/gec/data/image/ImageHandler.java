package gec.data.image;

import gec.data.GameMetaData;

import java.awt.image.BufferedImage;

public interface ImageHandler {
    String IMAGE_NAME = "preview.png";

    BufferedImage downloadImage(GameMetaData gameMetaData);

    BufferedImage loadImageFromDisk(String absoluteFilePath);
}
