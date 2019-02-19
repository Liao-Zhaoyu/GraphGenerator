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

        int N = 1000; // Number of nodes (positive integer)
        int T = 0; // Type of edges (0 = undirected, 1 = directed)
        int D = 1; // Statistical distribution of edges (0 = normal, 1 = powerlaw)
        int M = 0; // Generation method (0 = Matrix-based RMat, 1 = Array-based RMat)

        System.out.println("===Begin===");
        try {
            if (M == 0) {
                RMatMatrixApp rm = new RMatMatrixApp(N, T, D);
                rm.GenerateEdgeList();
                //rm.GenerateGraphML();

            } else {
                RMatArrayApp rm = new RMatArrayApp(N, T, D);
                rm.GenerateEdgeList();
                //rm.GenerateGraphML();
            }
        } catch (Exception ex) {
            System.out.println("Error during generation process");
            System.out.println(ex);
            return;
        }

        System.out.println("===End===");

    }

}
