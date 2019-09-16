/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: Sep 10, 2019
 *  Description: Entry for Seam
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {
    Picture picture;
    double energyArray[][];
    // private boolean marked[][];

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.energyArray = new double[width()][height()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                energyArray[col][row] = calculateEnergy(col, row);
            }
        }

    }

    class Pixel {
        int x;
        int y;
        double energy;

        public Pixel(int x, int y, double energy) {
            this.x = x;
            this.y = y;
            this.energy = energy;
        }

        public String toString() {
            return "x: " + x + " y: " + y + " energy: " + energy;
        }
    }

    // class PixelEdge {
    //     private final Pixel v;
    //     private final Pixel w;
    //     private final double weight;
    //
    //     public PixelEdge(Pixel v, Pixel w, double weight) {
    //         this.v = v;
    //         this.w = w;
    //         this.weight = weight;
    //     }
    //
    //     public Pixel from() {
    //         return v;
    //     }
    //
    //     public Pixel to() {
    //         return w;
    //     }
    // }
    //
    // class PixelGraph {
    //     private Bag<PixelEdge>[] adj;    // adj[v] = adjacency list for vertex v
    //
    //     public PixelGraph() {
    //
    //     }
    //
    //     public void addEdge(PixelEdge e) {
    //         adj[e.from()].add(e.to());
    //     }
    // }

    private void addPixel(int x, int y, ArrayList<Pixel> vertices, ArrayList<Pixel> edgeTo,
                          ArrayList<Double> energyTo) {
        boolean debug = false;

        // StdOut.println("Currently at x: " + x + " y: " + y);
        if (debug) StdOut.println("Currently at: " + new Pixel(x, y, energy(x, y)));
        for (int i = -1; i < 2; i++) {
            if (x + i < 0 || x + i > width() - 1) continue;
            vertices.add(new Pixel(x, y, energy(x, y)));
            edgeTo.add(new Pixel(x + i, y + 1, energy(x + i, y + 1)));
            energyTo.add(energy(x + i, y + 1));
        }
    }

    private void relax(int x1, int y1, int x2, int y2, double energyTo[][], Pixel[][] pixelTo) {
        if (energyTo[x2][y2] > energyTo[x1][y1] + energy(x2,y2)) {
            energyTo[x2][y2] = energyTo[x1][y1] + energy(x2,y2);
            pixelTo[x2][y2] = new Pixel(x1,y1, energy(x1,y1));
        }
    }

    private void travesePixel(int x, int y, double energyTo[][], Pixel pixelTo[][]) {
        boolean debug = false;

        // StdOut.println("Currently at x: " + x + " y: " + y);
        if (debug) StdOut.println("Currently at: " + new Pixel(x, y, energy(x, y)));
        for (int i = -1; i < 2; i++) {
            if (x + i < 0 || x + i > width() - 1) continue;
            relax(x,y,x+i, i+1, energyTo, pixelTo);
        //    from: x,y
        //    to: x-1,y+1;x,y+1; x+1, y+1

        }
    }


    // traverse pixels in topological order
    public void traverseDownFromPixel(int x, int y) {
        boolean debug = true;
        if (x < 0 || x > width() || y < 0 || y > height())
            throw new IllegalArgumentException("invalid starting index");

        double energyTo[][] = new double[width()][height()];
        Pixel pixelTo[][] = new Pixel[width()][height()];
        for (int i =0; i < height()-1; i++) {
            for (int k=0; k < width()-1; k++) {
                energyTo[i][k]= Double.POSITIVE_INFINITY;
            }
        }

        for (int i = y + 1; i < height() - 1; i++) {
            for (int k = x - i; k <= x + i; k++) {
                if (k < 0 || k > width() - 1) continue;
                travesePixel(k,i,energyTo,pixelTo);
            }
        }

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
