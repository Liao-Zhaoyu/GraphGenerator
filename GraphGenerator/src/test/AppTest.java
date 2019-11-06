package test;
import rmat.RMatArrayApp;
import rmat.RMatMatrixApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class AppTest {

    public static void main(String[] args) {

        long n = 1000; // -n <integer> // Number of nodes (mandatory)
        int t = 1; // -t [0|1]     // Type of edges: 0 = undirected, 1 = directed (default)
        int d = 1; // -d [0|1]     // Statistical distribution of edges: 0 = normal, 1 = powerlaw
        int m = 1; // -m [0|1|2]   // Generation method: 0 = Matrix, 1 = Primitive Array (default), 2 = Hash-based Array
        int f = 1; // -f [0|1]     // Output data format: 0 = edge list (default), 1 = graphml
        long s = 0; // -s <integer> // Random seed
        
        System.out.println("===Begin===");
        try {
            if (m == 0) {
                RMatMatrixApp rm = new RMatMatrixApp(n, t, d, f, s);
                rm.Run();

            } else {
                RMatArrayApp rm = new RMatArrayApp(n, t, d, m, f, s);
                rm.Run();
            }
        } catch (Exception ex) {
            System.out.println("Error during generation process");
            System.out.println(ex);
            return;
        }

        System.out.println("===End===");

    }

}
