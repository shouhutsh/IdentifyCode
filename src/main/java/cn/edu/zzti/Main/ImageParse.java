package cn.edu.zzti.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class ImageParse {

    public static String parse(BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int RGB = img.getRGB(i, j);
                int r = (RGB >>> 16) & 0xFF;
                int g = (RGB >>>  8) & 0xFF;
                int b = (RGB >>>  0) & 0xFF;

                if((r + g + b) < 0XFF * 1.5){
                    sb.append("*");
                } else {
                    sb.append(" ");
                }
            }
        }

        return sb.toString();
    }

    private static void dispImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();


        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int RGB = image.getRGB(i, j);
                int r = (RGB >> 16) & 0xFF;
                int g = (RGB >>  8) & 0xFF;
                int b = (RGB >>  0) & 0xFF;

                if((r + g + b) < 0XFF * 1.5)
                    System.out.printf("[]");
                else
                    System.out.printf("  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/image.png");
        BufferedImage image = ImageIO.read(file);
        dispImage(image);
    }
}
