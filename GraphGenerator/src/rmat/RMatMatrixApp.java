/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmat;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

/**
 *
 * @author renzo
 */
public class RMatMatrixApp {

    private int edge_type = 0;
    private int dist_type = 0;
    private long nodes = 0;
    private long edges = 0;
    private long edges_final = 0;
    int matrix[][];
    //R-MAT Probabilities
    double alpha = 0.6; //0.6
    double beta = 0.15; //0.15
    double gamma = 0.15; //0.15
    double delta = 0.10; //0.10
    private double offset1;
    private double offset2;
    private double offset3;
    private double offset4;
    int seed = 80808080;
    Random rand;

    /*
    Parameters:
    _nodes_number = number of nodes of the graph
    _edge_type = Type of edges (0 = undirected, 1 = directed)
    _distribution_type = Statistical distribution of edges (0 = normal, 1 = powerlaw)
     */
    public RMatMatrixApp(long _nodes_number, int _edge_type, int _distribution_type) {
        rand = new Random(_nodes_number);
        this.edge_type = _edge_type;
        this.dist_type = _distribution_type;
        if (this.dist_type == 0) {
            this.alpha = 0.25;
            this.beta = 0.25;
            this.gamma = 0.25;
            this.delta = 0.25;
        }
        nodes = _nodes_number;
        edges = (int) (Math.log(nodes) * nodes); //natural logarithm, base e = 2,7182818
        matrix = new int[(int) nodes][(int) nodes];
    }

    public long nodesNumber() {
        return this.nodes;
    }

    public long edgesNumber() {
        return this.edges_final;
    }

    public void GenerateEdgeList() {
        try {
            long e = 1;
            long pair[];
            long itime = System.currentTimeMillis();
            String filename = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-0.txt";
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            String line;
            System.out.println("Writing edges ...");
            int N = (int) nodes;
            int n1;
            int n2;
            while (e <= edges) {
                pair = this.chooseEdge(1, 1, N, N, alpha, beta, gamma, delta);
                n1 = (int) pair[0] - 1;
                n2 = (int) pair[1] - 1;
                if (this.edge_type == 0 && matrix[n1][n2] == 0 && matrix[n2][n1] == 0) {
                    matrix[n1][n2] = 1;
                    matrix[n2][n1] = 1;
                    e++;
                    line = n1 + " " + n2 + "\n";
                    writer.write(line);
                }
                if (this.edge_type == 1 && matrix[n1][n2] == 0) {
                    matrix[n1][n2] = 1;
                    e++;
                    line = n1 + " " + n2 + "\n";
                    writer.write(line);
                }                
            }
            writer.close();
            long etime = System.currentTimeMillis() - itime;

            line = "Number of nodes: " + nodes + "\n";
            line += "Number of edges: " + e + "\n";
            line += "Execution time: " + etime + " ms";
            System.out.println(line);
            this.edges_final = e;

            String logfile = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-0.log";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile), "UTF-8"));
            writer.write(line);
            writer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

    }

    public void GenerateGraphML() {
        long pair[];
        long itime = System.currentTimeMillis();
        try {
            String filename = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-0.graphml";
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.write("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
            writer.write("\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            writer.write("\n\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
            writer.write("\n\thttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
            if (this.edge_type == 1) {
                writer.write("\n\t<graph id=\"G\" edgedefault=\"directed\" >");
            } else {
                writer.write("\n\t<graph id=\"G\" edgedefault=\"undirected\" >");
            }
            String line;
            System.out.println("Writing nodes ...");
            for (int n = 0; n < nodes; n++) {
                line = "\n\t<node id=\"" + n + "\" />";
                writer.write(line);
            }

            System.out.println("Writing edges ...");

            int N = (int) nodes;
            long e = 1;
            int n1;
            int n2;
            while (e <= edges) {
                pair = this.chooseEdge(1, 1, N, N, alpha, beta, gamma, delta);
                n1 = (int) pair[0] - 1;
                n2 = (int) pair[1] - 1;
                if (edge_type == 0 && matrix[n1][n2] == 0 && matrix[n2][n1] == 0) {
                    matrix[n1][n2] = 1;
                    matrix[n2][n1] = 1;                    
                    e++;
                    line = "\n\t<edge id=\"" + e + "\" source=\"" + n1 + "\" target=\"" + n2 + "\"/>";
                    writer.write(line);
                }
                if (edge_type == 1 && matrix[n1][n2] == 0) {
                    matrix[n1][n2] = 1;
                    e++;
                    line = "\n\t<edge id=\"" + e + "\" source=\"" + n1 + "\" target=\"" + n2 + "\"/>";
                    writer.write(line);
                }                
            }

            writer.write("\n\t</graph>");
            writer.write("\n</graphml>");
            writer.close();
            System.out.println("Number of nodes: " + nodes);
            System.out.println("Number of edges: " + e);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        long etime = System.currentTimeMillis() - itime;
        System.out.println("Execution time: " + etime + "ms (" + etime / 1000 + "s)");

    }

    //método que simula R-Mat y retorna un arista
    //recursivamente divide la matriz de adyacencia
    public long[] chooseEdge(long x1, long y1, long xn, long yn, double a, double b, double c, double d) {

        if (x1 >= xn || y1 >= yn) {
            long pair[] = new long[2];
            pair[0] = x1;
            pair[1] = y1;
            return pair;
        }
        double ab = a + b;
        double abc = a + b + c;
        double new_a = Math.abs(a + offset1);
        double new_b = Math.abs(b + offset2);
        double new_c = Math.abs(c + offset3);
        double new_d = Math.abs(d + offset4);

        double r = rand.nextDouble();
        long halfx;
        long halfy;
        if (r < a) {
            halfx = (long) Math.ceil((x1 + xn) / 2);
            halfy = (long) Math.ceil((y1 + yn) / 2);
            return chooseEdge(x1, y1, halfx, halfy, new_a, new_b, new_c, new_d);
        } else if (r >= a && r < ab) {

            halfx = (long) Math.floor((x1 + xn) / 2);
            halfy = (long) Math.ceil((y1 + yn) / 2);
            return chooseEdge(halfx, y1, xn, halfy, new_a, new_b, new_c, new_d);

        } else if (r >= ab && r < abc) {
            halfx = (long) Math.ceil((x1 + xn) / 2);
            halfy = (long) Math.floor((y1 + yn) / 2);
            return chooseEdge(x1, halfy, halfx, yn, new_a, new_b, new_c, new_d);

        } else {
            halfx = (long) Math.floor((x1 + xn) / 2);
            halfy = (long) Math.floor((y1 + yn) / 2);
            return chooseEdge(halfx, halfy, xn, yn, new_a, new_b, new_c, new_d);
        }
    }

}
