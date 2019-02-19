
import rmat.RMatArrayApp;
import rmat.RMatMatrixApp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class MainApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage");
            System.out.println("java -jar GraphGenerator %N %T %D %M");
            System.out.println("%N : Number of nodes (positive integer)");
            System.out.println("%T : Type of edges (0 = undirected, 1 = directed)");
            System.out.println("%D : Statistical distribution of edges (0 = normal, 1 = powerlaw)");
            System.out.println("%M : Generation method (0 = Matrix-based RMat, 1 = Array-based RMat)");
            return;
        }

        int N = 0;
        int T = 0;
        int D = 0;
        int M = 0;
        try {
            N = Integer.parseInt(args[0]);
            if (N < 1) {
                throw new IllegalArgumentException("Invalid value for %N");
            }
            T = Integer.parseInt(args[1]);
            if (T < 0 || T > 1) {
                throw new IllegalArgumentException("Invalid value for %T");
            }
            D = Integer.parseInt(args[2]);
            if (D < 0 || D > 1) {
                throw new IllegalArgumentException("Invalid value for %T");
            }
            M = Integer.parseInt(args[3]);
            if (M < 0 || M > 1) {
                throw new IllegalArgumentException("Invalid value for %T");
            }
        } catch (Exception ex) {
            System.out.println("Error: Invalid parameters");
            System.out.println(ex);
            return;
        }

        System.out.println("===Begin===");
        System.out.println("Parameters:");
        System.out.println("N = " + N);
        System.out.println("T = " + T);
        System.out.println("D = " + D);
        System.out.println("M = " + M);
        try {
            if (M == 0) {
                RMatMatrixApp rm = new RMatMatrixApp(N, T, D);
                rm.GenerateEdgeList();

            } else {
                RMatArrayApp rm = new RMatArrayApp(N, T, D);
                rm.GenerateEdgeList();
            }
        } catch (Exception ex) {
            System.out.println("Error during generation process");
            System.out.println(ex);
            return;
        }

    }

}
