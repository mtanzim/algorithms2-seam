/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: Sep 10, 2019
 *  Description: Entry for Seam
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
    Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;

    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        if (x < 1 || x > width() - 2 || y < 1 || y > height() - 2)
            return 1000;
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color top = picture.get(x, y + 1);
        Color bottom = picture.get(x, y - 1);

        int deltaSqX = ((right.getRed() - left.getRed()) * (right.getRed() - left.getRed()))
                + ((right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen()))
                + ((right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue()));

        int deltaSqY = ((bottom.getRed() - top.getRed()) * (bottom.getRed() - top.getRed()))
                + ((bottom.getGreen() - top.getGreen()) * (bottom.getGreen() - top.getGreen()))
                + ((bottom.getBlue() - top.getBlue()) * (bottom.getBlue() - top.getBlue()));

        return Math.sqrt(deltaSqX * 1.0 + deltaSqY * 1.0);

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new int[] { 0, 0 };
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return new int[] { 0, 0 };

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public static void main(String[] args) {
        StdOut.println("Hello from Seam.");
    }
}
