package gec.data.image;

import gec.data.Game;

public interface ImageHandler {
    String IMAGE_NAME = "preview.png";

    void downloadImage(Game game);
}
