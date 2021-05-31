package gec.ui.utils;

import java.awt.*;

public class ImageHelper {
    public static Image rescaleImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_REPLICATE);
    }
}
