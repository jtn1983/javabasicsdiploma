package ru.netology.graphics;

import ru.netology.graphics.image.BadImageSizeException;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GraphicsConverter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int width = img.getWidth();
        int height = img.getHeight();

        if (width == 0 || height == 0) {
            throw new BadImageSizeException();
        }

        if (maxRatio != 0) {
            double ratio = (double) width / height;
            if (maxRatio < ratio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }

        int[] newWidthHeight = calculateNewWidthHeight(width, height);
        int newWidth = newWidthHeight[0];
        int newHeight = newWidthHeight[1];

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        ImageIO.write(bwImg, "png", new File("out.png"));

        var bwRaster = bwImg.getRaster();

        if (schema == null) {
            schema = new ColorSchema();
        }

        char[][] textImage = new char[newHeight][newWidth];

        for (int i = 0; i < textImage.length; i++) {
            for (int j = 0; j < textImage[0].length; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                textImage[i][j] = c;
            }
        }

        StringBuilder textImg = new StringBuilder();
        for (int i = 0; i < textImage.length; i++) {
            for (int j = 0; j < textImage[0].length; j++) {
                textImg.append(textImage[i][j]);
                textImg.append(textImage[i][j]);
            }
            textImg.append("\n");
        }

        return textImg.toString();
    }

    private int[] calculateNewWidthHeight (int width, int height) {
        double ratioW = (double) maxWidth / width;
        double ratioH = (double) maxHeight / height;
        int newWidth = (int) (width * Math.min(ratioW, ratioH));
        int newHeight = (int) (height * Math.min(ratioW, ratioH));
        if (newWidth == 0) newWidth = width;
        if (newHeight == 0) newHeight = height;
        return new int[]{newWidth, newHeight};
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
