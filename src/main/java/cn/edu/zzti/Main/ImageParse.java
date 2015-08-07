package cn.edu.zzti.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ae-mp02 on 2015/8/6.
 */
public class ImageParse {

    private ImageParse() {
    }

    private static final char CHAR_SHOW = '*';
    private static final char CHAR_HIDE = ' ';
    private static final float THRESHOLD_SHOW_CHAR = 0X100 * 1.5F;
    private static final float THRESHOLD_CLEAN_CHAR = 5;
    private static final float THRESHOLD_CLEAN_LINE = 2;

    public static char[][] parse(BufferedImage img) {
        Rectangle rec = new Rectangle(img);
        char image[][] = new char[rec.height][rec.width];

        for (int y = 0; y < rec.height; y++) {
            for (int x = 0; x < rec.width; x++) {
                int RGB = img.getRGB(x, y);
                int r = (RGB >>> 16) & 0xFF;
                int g = (RGB >>> 8) & 0xFF;
                int b = (RGB >>> 0) & 0xFF;

                if ((r + g + b) < THRESHOLD_SHOW_CHAR) {
                    image[y][x] = CHAR_SHOW;
                } else {
                    image[y][x] = CHAR_HIDE;
                }
            }
        }
        return image;
    }

    public static void dispImage(char[][] image) {
        Rectangle rec = new Rectangle(image);

        for (int y = 0; y < rec.height; y++) {
            System.out.print("|");
            for (int x = 0; x < rec.width; x++) {
                System.out.printf("%c", image[y][x]);
            }
            System.out.println("|");
        }
    }

    private static char[][] cleanImage(char[][] old_img) {
        Rectangle rec = new Rectangle(old_img);
        char new_img[][] = new char[rec.height][rec.width];

        for (int y = 0; y < rec.height; y++) {
            for (int x = 0; x < rec.width; x++) {
                new_img[y][x] = getCleanChar(old_img, new Point(x, y));
            }
        }
        return new_img;
    }

    private static char[][] clipImage(char[][] old_img) {
        int x, y, w, h;
        Rectangle r = new Rectangle(old_img);

        lableX:
        for (x = 0; x < r.width; ++x) {
            int count = 0;
            for (int _y = 0; _y < r.height; ++_y) {
                if (old_img[_y][x] == CHAR_SHOW) ++count;
            }
            if (count > THRESHOLD_CLEAN_LINE) break lableX;
        }
        lableY:
        for (y = 0; y < r.height; ++y)
            for (int _x = 0; _x < r.width; ++_x)
                if (old_img[y][_x] == CHAR_SHOW) break lableY;
        lableW:
        for (w = r.width - 1; w > 0; --w) {
            int count = 0;
            for (int _h = 0; _h < r.height; ++_h) {
                if (old_img[_h][w] == CHAR_SHOW) ++count;
            }
            if (count > THRESHOLD_CLEAN_LINE) break lableW;
        }
        lableH:
        for (h = r.height - 1; h > 0; --h)
            for (int _w = 0; _w < r.width; ++_w)
                if (old_img[h][_w] == CHAR_SHOW) break lableH;

        char new_img[][] = new char[h - y + 1][w - x + 1];
        for (int _y = y; _y <= h; ++_y)
            for (int _x = x; _x <= w; ++_x)
                new_img[_y - y][_x - x] = old_img[_y][_x];
        return new_img;
    }

    private static List<char[][]> splitImageWithAverage(char[][] image, int num) {
        Rectangle r = new Rectangle(image);
        int h = r.height;
        int w = (r.width + num - 1) / num;

        List<char[][]> imgs = new ArrayList<char[][]>(num);
        for (int i = 1; i <= num; ++i) {
            // 我每次多取一列以提高精确度
            char[][] img = new char[h][w + 1];
            for (int j = 0; j < h; ++j) {
                for (int k = (w * (i - 1) - 1); k < w * i; ++k) {
                    if (k < 0 || k >= r.width)
                        img[j][k - (w * (i - 1) - 1)] = ' ';
                    else
                        img[j][k - (w * (i - 1) - 1)] = image[j][k];
                }
            }
            imgs.add(img);
        }
        return imgs;
    }

    // FIXME
    private static List<char[][]> splitImageWithBlank(char[][] image) {
        final int START = 0;
        final int END = 1;

        Rectangle r = new Rectangle(image);
        List<Integer> splitPoint = new ArrayList<Integer>();


        int state = START;
        splitPoint.add(0);
        for (int x = 0; x < r.width; ++x) {
            int count = 0;
            if (state == START) {
                for (int y = 0; y < r.height; ++y)
                    if (image[y][x] == CHAR_SHOW) ++count;
                if (count <= 0) {
                    splitPoint.add(x);
                    state = END;
                }
            } else if (state == END) {
                for (int y = 0; y < r.height; ++y)
                    if (image[y][x] == CHAR_SHOW) ++count;
                if (count > 0) {
                    splitPoint.add(x);
                    state = START;
                }
            }
        }
        splitPoint.add(r.width);


        List<char[][]> imgs = new ArrayList<char[][]>(splitPoint.size() / 2);
        for (int i = 0; i < splitPoint.size(); i += 2) {
            int s = splitPoint.get(i);
            int e = splitPoint.get(i + 1);
            char[][] img = new char[r.height][e - s];
            for (int y = 0; y < r.height; ++y)
                for (int x = s; x < e; ++x)
                    img[y][x - s] = image[y][x];
            imgs.add(img);
        }
        return imgs;
    }

    // TODO 使用广度优先算法将干扰点去除
    private static char getCleanChar(char[][] image, Point p) {
        if (image[p.y][p.x] == CHAR_HIDE) return CHAR_HIDE;

        Rectangle rec = new Rectangle(image);

        int count = 0;
        for (int h = p.y - 1; h <= p.y + 1; ++h) {
            for (int w = p.x - 1; w <= p.x + 1; ++w) {
                // 在Image四角时会发生重复计数的情况，但是一个点并不影响什么
                if ((w >= 0 && h >= 0)
                        && (w < rec.width && h < rec.height)
                        && image[h][w] == CHAR_SHOW) {
                    continue;
                }
                ++count;
            }
        }
        return count >= THRESHOLD_CLEAN_CHAR ? CHAR_HIDE : CHAR_SHOW;
    }

    private static class Point {
        public final int x;
        public final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Rectangle {
        public final int width;
        public final int height;

        public Rectangle(char[][] image) {
            this(image[0].length, image.length);
        }

        public Rectangle(BufferedImage image) {
            this(image.getWidth(), image.getHeight());
        }

        private Rectangle(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/image.jpg");
        char[][] image = parse(ImageIO.read(file));
        char[][] clean = cleanImage(image);
        char[][] clip = clipImage(clean);

        dispImage(image);
        dispImage(clean);
        dispImage(clip);

        List<char[][]> imgs = splitImageWithBlank(clip);
        for (char[][] img : imgs) {
            dispImage(clipImage(img));
        }
    }
}
