/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author renzo
 */
public class RMatArrayApi {

    private int edge_type = 0;
    private int dist_type = 0;
    private long nodes = 0;
    private long edges = 0;
    private long edges_final = 0;

    //R-MAT Probabilities
    double alpha = 0.6;
    double beta = 0.15;
    double gamma = 0.15;
    double delta = 0.10;
    private double offset1;
    private double offset2;
    private double offset3;
    private double offset4;
    int seed = 80808080;
    Random rand;

    private int source_node;
    private int target_node;
    private int aux_target_node = 0;
    private long missed = 0;
    long[] out_dist;
    long[] in_dist;
    long[] dist;

    /*
    Parameters:
    nodes_number = number of nodes of the graph
    _edge_type = Type of edges (0 = undirected, 1 = directed)
    distribution_type = Statistical distribution of edges (0 = normal, 1 = powerlaw)
     */
    public RMatArrayApi(long nodes_number, int _edge_type, int distribution_type) {
        rand = new Random(nodes_number);
        this.edge_type = _edge_type;
        this.dist_type = distribution_type;
        if (this.dist_type == 0) {
            this.alpha = 0.25;
            this.beta = 0.25;
            this.gamma = 0.25;
            this.delta = 0.25;
        }
        nodes = nodes_number;
        edges = (int) (Math.log(nodes) * nodes);
        System.out.println("N=" + nodes);
        System.out.println("E=" + edges);

        //inicializacion
        if (this.edge_type == 0) {
            dist = this.GenerateDistribution((int) nodes, edges);
            source_node = 0;
            target_node = source_node + 1;
        } else {
            out_dist = this.GenerateDistribution((int) nodes, edges);
            in_dist = this.CopyDist(out_dist);
            source_node = 0;
            target_node = 0;
        }

    }

    public Edge nextEdge() {
        if (this.edge_type == 0) {
            return nextUndirectedEdge();
        } else {
            return nextDirectedEdge();
        }
    }

    private Edge nextUndirectedEdge() {
        Edge edge;
        while (source_node < dist.length) {
            if (dist[source_node] > 0) {
                while (target_node < dist.length) {
                    if (dist[target_node] > 0) {
                        dist[source_node] = dist[source_node] - 1;
                        dist[target_node] = dist[target_node] - 1;
                        edge = new Edge(source_node, target_node);
                        target_node++;
                        return edge;
                    }
                    target_node++;
                }
                //There are no enough target nodes; then, use nodes from the beginning
                dist[source_node] = dist[source_node] - 1;
                edge = new Edge(source_node, aux_target_node);
                aux_target_node++;
                return edge;
            }
            source_node++;
            target_node = source_node + 1;
        }
        return null;
    }

    private Edge nextDirectedEdge() {
        Edge edge;
        while (source_node < out_dist.length) {
            if (out_dist[source_node] > 0) {
                while (target_node < in_dist.length) {
                    if (source_node != target_node && in_dist[target_node] > 0) {
                        out_dist[source_node] = out_dist[source_node] - 1;
                        in_dist[target_node] = in_dist[target_node] - 1;
                        edge = new Edge(source_node, target_node);
                        target_node++;
                        return edge;
                    }
                    target_node++;
                }
                //There are no enough target nodes; then, use nodes from the beginning
                out_dist[source_node] = out_dist[source_node] - 1;
                edge = new Edge(source_node, aux_target_node);
                aux_target_node++;
                return edge;                
            }
            source_node++;
            target_node = 0;
        }
        return null;
    }

    //método que construye una distribucion de degrees simulando R-Mat
    private long[] GenerateDistribution(int N, long E) {
        long[] array = new long[N];
        long pair[];
        //R-MAT Probabilities
        double a = alpha;
        double b = beta;
        double c = gamma;
        double d = delta;

        double m = 0.25;
        double depth = Math.ceil(Math.log(N) / Math.log(2));
        offset1 = (m - a) / depth;
        offset2 = (m - b) / depth;
        offset3 = (m - c) / depth;
        offset4 = (m - d) / depth;

        int n1 = 0;
        int n2 = 0;
        for (int j = 0; j < E; j++) {
            do {
                pair = this.chooseEdge(1, 1, N + 1, N + 1, a, b, c, d);
                n1 = (int) pair[0];
                n2 = (int) pair[1];
            } while (array[n1 - 1] >= N || array[n2 - 1] >= N);
            array[n1 - 1] = array[n1 - 1] + 1;
            if (this.edge_type == 0) {
                array[n2 - 1] = array[n2 - 1] + 1;
            }
        }

        //a los elementos con 0, les asigno 1 
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = 1;
            }
        }
        return array;
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

    private long[] CopyDist(long[] dist1) {
        long[] dist2 = new long[dist1.length];
        System.arraycopy(dist1, 0, dist2, 0, dist1.length);
        int idx;
        long tmp;
        for (int i = 0; i < dist2.length; i++) {
            idx = rand.nextInt(dist2.length);
            tmp = dist2[i];
            dist2[i] = dist2[idx];
            dist2[idx] = tmp;
        }
        return dist2;
    }

}
