package com.example.demo.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;

public final class ImageResizer {

    private static final int TARGET_SIZE = 256;

    private ImageResizer() {}

    @SneakyThrows
    public static File resizeTo256x256(File source) {
        BufferedImage original = ImageIO.read(source);
        BufferedImage resized = new BufferedImage(TARGET_SIZE, TARGET_SIZE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = resized.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(original, 0, 0, TARGET_SIZE, TARGET_SIZE, null);
        graphics.dispose();

        File output = File.createTempFile("thumbnail-", ".png");
        ImageIO.write(resized, "png", output);
        return output;
    }
}

