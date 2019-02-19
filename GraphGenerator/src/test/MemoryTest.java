package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renzo
 */
public class MemoryTest {

    public static void main(String[] args) {
        //N = 21.252 es el límite máximo para crear un array
        for (int N = 21252; N < 100000; N++) {
            System.out.println("Generating matrix of " + N + " nodes");
            int matrix[][] = new int[N][N];
            int n = 1;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    matrix[i][j] = n;
                }
                n++;
            }
            System.out.println("OK");
        }

    }

}
