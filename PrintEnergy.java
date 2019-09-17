/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(),
                      picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.energy(col, row));
            StdOut.println();
        }
        Stopwatch sw = new Stopwatch();
        int[] sp = sc.findVerticalSeam();
        StdOut.println("Seam finding time: " + sw.elapsedTime() + " seconds.");

        StdOut.println("SHORTEST SEAM: ");
        StdOut.println(Arrays.toString(sp));

        // for (int i = 0; i < sc.width(); i++) {
        //     sc.traverseDownFromPixel(i,0);
        //
        // }
    }

}
