package cn.edu.zzti.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class ImageParse {

    private ImageParse() {
    }

    private static final char CHAR_SHOW = '*';
    private static final char CHAR_HIDE = ' ';
    private static final float THRESHOLD = 1.5F;

    public static char[][] parse(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        char image[][] = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int RGB = img.getRGB(x, y);
                int r = (RGB >>> 16) & 0xFF;
                int g = (RGB >>> 8) & 0xFF;
                int b = (RGB >>> 0) & 0xFF;

                if ((r + g + b) < 0XFF * THRESHOLD) {
                    image[y][x] = CHAR_SHOW;
                } else {
                    image[y][x] = CHAR_HIDE;
                }
            }
        }
        return image;
    }

    public static void dispImage(char[][] image) {
        int height = image.length;
        int width = image[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.printf("%c", image[y][x]);
            }
            System.out.println();
        }
    }

    private static char[][] clean(char[][] old_img) {
        int height = old_img.length;
        int width = old_img[0].length;
        char new_img[][] = new char[height][width];

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                new_img[y][x] = cleanChar(old_img, x, y);
            }
        }
        return new_img;
    }

    // FIXME
    private static char cleanChar(char[][] image, int x, int y) {
        if (image[y][x] == CHAR_HIDE) return CHAR_HIDE;

        int height = image.length;
        int width = image[0].length;

        int count = 0;
        for (int h = y - 1; h <= y + 1; ++h) {
            for (int w = x - 1; w <= x + 1; ++w) {
                if ((w >= 0 && h >= 0)
                        && (w < width && h < height)
                        && (w != x && h != y)
                        && image[h][w] == CHAR_SHOW) {
                    continue;
                }
                ++count;
            }
        }
        return count >= 8 ? CHAR_HIDE : CHAR_SHOW;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/image.jpg");
        char[][] image = parse(ImageIO.read(file));
        dispImage(image);
        dispImage(clean(image));
    }
}
