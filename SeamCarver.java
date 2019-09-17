/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: Sep 10, 2019
 *  Description: Entry for Seam
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    static final int START_INDICATOR = -1;
    Picture picture;
    private double[][] energyArray;
    private double[][] energyArrayTransposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        calculateEnergy();
    }

    private void calculateEnergy(){
        Stopwatch sw = new Stopwatch();
        boolean debug = true;
        this.energyArray = new double[width()][height()];
        this.energyArrayTransposed = new double[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                double curEnergy = calculateEnergy(col, row);
                energyArray[col][row] = curEnergy;
                energyArrayTransposed[row][col] = curEnergy;
            }
        }
        if (debug) {
            StdOut.println("Energy calculation time: " + sw.elapsedTime() + " seconds.");
        }
    }

    class Pixel {
        int x;
        int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return x + "," + y;
        }
    }

    class SPNode implements Comparable<SPNode> {
        double energy;
        int[] path;

        public SPNode(double energy, int[] path) {
            this.energy = energy;
            this.path = path;
        }

        @Override
        public int compareTo(SPNode that) {
            return Double.compare(this.energy, that.energy);
        }
    }

    private void relax(int x1, int y1, int x2, int y2, double[][] localEnergy, double[][] energyTo, Pixel[][] pixelTo) {
        boolean debug = false;
        if (debug)
            StdOut.println("Relaxing edge to " + (new Pixel(x2, y2).toString()));
        if (energyTo[x2][y2] > energyTo[x1][y1] + localEnergy[x2][y2]) {
            energyTo[x2][y2] = energyTo[x1][y1] + localEnergy[x2][y2];
            pixelTo[x2][y2] = new Pixel(x1, y1);
        }
    }

    // traverse pixels in topological order, and visit all of its
    // adjancent(connected) pixels
    private SPNode traverseDownFromPixel(int x, int y, boolean isTransposed) {
        boolean debug = false;
        double[][] localEnergy = energyArray;
        int localWidth = width();
        int localHeight = height();

        if (isTransposed) {
            localEnergy = energyArrayTransposed;
            localHeight = width();
            localWidth = height();
        }
        if (x < 0 || x > localWidth || y < 0 || y > localHeight)
            throw new IllegalArgumentException("invalid starting index");

        double[][] energyTo = new double[localWidth][localHeight];
        Pixel[][] pixelTo = new Pixel[localWidth][localHeight];

        for (int i = 0; i < localHeight; i++) {
            for (int k = 0; k < localWidth; k++) {
                energyTo[k][i] = Double.POSITIVE_INFINITY;
            }
        }
        // starting point
        energyTo[x][y] = 0.0;
        pixelTo[x][y] = new Pixel(START_INDICATOR, START_INDICATOR);

        for (int i = y; i < localHeight - 1; i++) {
            for (int k = x - i; k <= x + i; k++) {
                if (k < 0 || k > localWidth - 1)
                    continue;
                // relax adjacent pixels
                for (int m = -1; m < 2; m++) {
                    if (k + m < 0 || k + m > localWidth - 1)
                        continue;
                    relax(k, i, k + m, i + 1, localEnergy, energyTo, pixelTo);
                }
            }
        }
        // algo for finding shortest path:
        // - find min of last row
        // - traverse up using min bottom pixel
        double minTotalEnergy = energyTo[0][localHeight - 1];
        Pixel bottomPixel = new Pixel(0, localHeight - 1);

        for (int i = 1; i < localWidth; i++) {
            if (minTotalEnergy > energyTo[i][localHeight - 1]) {
                minTotalEnergy = energyTo[i][localHeight - 1];
                bottomPixel = new Pixel(i, localHeight - 1);
            }
        }

        // now traverse up the chain
        int[] path = new int[localHeight];
        Pixel curPixel = bottomPixel;
        int tracker = localHeight - 1;
        while (curPixel.x != START_INDICATOR) {
            path[tracker] = curPixel.x;
            curPixel = pixelTo[curPixel.x][curPixel.y];
            tracker--;
        }

        if (debug) {
            for (int row = 0; row < localHeight; row++) {
                for (int col = 0; col < localWidth; col++)
                    StdOut.printf("%9.2f ", energyTo[col][row]);
                StdOut.println();
            }
            StdOut.println();
            for (int row = 0; row < localHeight; row++) {
                for (int col = 0; col < localWidth; col++) {
                    if (pixelTo[col][row] == null) {

                        StdOut.printf("%9s", "BLK");
                        continue;
                    }
                    StdOut.printf("%9s", pixelTo[col][row].toString());
                }
                StdOut.println();
            }
            StdOut.println();
            StdOut.println("bottom pixel: " + bottomPixel.toString());
            StdOut.println("min total energy: " + minTotalEnergy);
            StdOut.println("path: " + Arrays.toString(path));

        }

        return new SPNode(minTotalEnergy, path);

    }

    // energy of pixel at column x and row y
    private double calculateEnergy(int x, int y) {

        if (x < 1 || x > width() - 2 || y < 1 || y > height() - 2)
            return 1000;
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color top = picture.get(x, y + 1);
        Color bottom = picture.get(x, y - 1);

        int rr = right.getRed();
        int rg = right.getGreen();
        int rb = right.getBlue();
        int lr = left.getRed();
        int lg = left.getGreen();
        int lb = left.getBlue();
        int tr = top.getRed();
        int tg = top.getGreen();
        int tb = top.getBlue();
        int br = bottom.getRed();
        int bg = bottom.getGreen();
        int bb = bottom.getBlue();

        int deltaSqX = ((rr - lr) * (rr - lr)) + ((rg - lg) * (rg - lg)) + ((rb - lb) * (rb - lb));

        int deltaSqY = ((br - tr) * (br - tr)) + ((bg - tg) * (bg - tg)) + ((bb - tb) * (bb - tb));

        return Math.sqrt(deltaSqX * 1.0 + deltaSqY * 1.0);

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

    public double energy(int x, int y) {
        return energyArray[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        boolean debug = true;
        Stopwatch sw = new Stopwatch();

        MinPQ<SPNode> mpq = new MinPQ<SPNode>();
        for (int i = 0; i < height(); i++) {
            mpq.insert(traverseDownFromPixel(i, 0, true));

        }
        int[] sp = mpq.delMin().path;
        if (debug) {
            // StdOut.println(Arrays.toString(sp));
            StdOut.println("Horizontal seam finding time: " + sw.elapsedTime() + " seconds.");
        }
        return sp;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        boolean debug = true;

        Stopwatch sw = new Stopwatch();
        MinPQ<SPNode> mpq = new MinPQ<SPNode>();
        for (int i = 0; i < width(); i++) {
            mpq.insert(traverseDownFromPixel(i, 0, false));

        }
        int[] sp = mpq.delMin().path;
        if (debug) {
            // StdOut.println(Arrays.toString(sp));
            StdOut.println("Vertical seam finding time: " + sw.elapsedTime() + " seconds.");
        }
        return sp;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
    }

    public void removeVerticalSeam(int[] seam) {
        boolean debug = false;
        Stopwatch sw = new Stopwatch();
        Picture newPic = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            int adder = 0;
            for (int k = 0; k < width(); k++) {
                if (k == seam[i]) {
                    adder = 1;
                    continue;
                }
                newPic.setRGB(k - adder, i, picture.getRGB(k, i));
            }
        }
        this.picture = newPic;
        if (debug) {
            StdOut.println("Seam removal time: " + sw.elapsedTime() + " seconds.");
        }
    }

    // remove vertical seam from current picture
    public static void main(String[] args) {
        StdOut.println("Hello from Seam.");
    }
}
