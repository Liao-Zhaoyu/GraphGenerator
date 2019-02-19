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
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author renzo
 */
public class RMatArrayApp {

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

    private int UNDIRECTED = 0;
    private int DIRECTED = 1;

    /*
    Parameters:
    nodes_number = number of nodes of the graph
    _edge_type = Type of edges (0 = undirected, 1 = directed)
    distribution_type = Statistical distribution of edges (0 = normal, 1 = powerlaw)
     */
    public RMatArrayApp(long nodes_number, int _edge_type, int distribution_type) {
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
        System.out.println("Number of nodes: " + nodes);
        System.out.println("Number of edges: " + edges);
    }

    public long nodesNumber() {
        return this.nodes;
    }

    public long edgesNumber() {
        return this.edges_final;
    }

    public void GenerateEdgeList() {
        try {
            String filename = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-1.txt";
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            System.out.println("Writing edges ...");
            long itime = System.currentTimeMillis();
            if (this.edge_type == 0) {
                this.GenerateUndirectedEdges(writer, 1);
            } else {
                this.GenerateDirectedEdges(writer, 1);
            }
            writer.close();
            long etime = System.currentTimeMillis() - itime;

            String line = "Final number of nodes: " + nodes + "\n";
            line += "Final number of edges: " + this.edges_final + "\n";
            line += "Execution time: " + etime + " ms";
            System.out.println(line);

            String logfile = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-1.log";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile), "UTF-8"));
            writer.write(line);
            writer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void GenerateGraphML() {
        long itime = System.currentTimeMillis();
        try {
            String filename = "graph-" + nodes + "-" + edge_type + "-" + dist_type + "-1.graphml";
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
            if (this.edge_type == 0) {
                this.GenerateUndirectedEdges(writer, 2);
            } else {
                this.GenerateDirectedEdges(writer, 2);
            }

            writer.write("\n\t</graph>");
            writer.write("\n</graphml>");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        long etime = System.currentTimeMillis() - itime;
        System.out.println("Execution time: " + etime + "ms (" + etime / 1000 + "s)");

    }


    /*
    Method to generate an undirected graph 
    Dado un nodo X, la selección de vecinos se basa en elegir los nodos 
    consecutivos a X en el array de nodos
     */
    private void GenerateUndirectedEdges(Writer writer, int format) throws Exception {
        int source_node = 0;
        int target_node;
        long missed_edges = 0;
        int e = 0;
        String line;
        long[] dist = this.GenerateDistribution((int) nodes, edges);
        while (source_node < dist.length) {
            target_node = source_node + 1;
            while (target_node < dist.length) {
                if (dist[source_node] <= 0) {
                    break;
                }
                if (dist[target_node] > 0) {
                    dist[source_node] = dist[source_node] - 1;
                    dist[target_node] = dist[target_node] - 1;
                    if (format == 1) {
                        //edge list format
                        line = source_node + " " + target_node + "\n";
                    } else {
                        //graphml format
                        line = "\n\t<edge id=\"" + e + "\" source=\"" + source_node + "\" target=\"" + target_node + "\"/>";
                    }
                    writer.write(line);
                    e++;
                }
                target_node++;
            }
            if (dist[source_node] > 0) {
                missed_edges = missed_edges + dist[source_node];
            }
            source_node++;
        }
        System.out.println("Missed edges: " + missed_edges);
        this.edges_final = e;
    }

    /*
    Método para la generación de aristas dirigidos
    Dado un nodo X, la selección de vecinos se basa en elegir los nodos 
    consecutivos a X en el array de nodos (similar a GenerateUndirectedEdges).
    La diferencia radica en que se emplean dos arrays de degrees: 
    - out_dist que registra las aristas salientes, y
    - in_degree que registra las aristas entrantes
     */
    private void GenerateDirectedEdges(Writer writer, int format) throws Exception {
        int e = 0;
        long missed_edges = 0;
        String line;
        System.out.println("Generation of out_dist array");
        long[] out_dist = this.GenerateDistribution((int) nodes, edges);
        System.out.println("Generation of in_dist array");
        long[] in_dist = this.GenerateDistribution((int) nodes, edges);
        //long[] in_dist = this.CopyDist(out_dist);
        System.out.println("Generation of edges");
        int source_node = 0;
        int target_node;
        int first_target_node = 0;
        boolean flag;
        while (source_node < out_dist.length) {
            target_node = first_target_node;
            flag = true;
            while (out_dist[source_node] > 0 && target_node < in_dist.length) {
                if (target_node != source_node && in_dist[target_node] > 0) {
                    out_dist[source_node] = out_dist[source_node] - 1;
                    in_dist[target_node] = in_dist[target_node] - 1;
                    if (format == 1) {
                        //edge list format
                        line = source_node + " " + target_node + "\n";
                    } else {
                        //graphml format
                        line = "\n\t<edge id=\"" + e + "\" source=\"" + source_node + "\" target=\"" + target_node + "\"/>";
                    }
                    writer.write(line);
                    e++;
                }
                //optimization: save the first position i in out_dist such that out_dist[i] > 0 
                if (flag && in_dist[target_node] > 0) {
                    first_target_node = target_node;
                    flag = false;
                }
                target_node++;
            }
            if (out_dist[source_node] > 0) {
                missed_edges = missed_edges + out_dist[source_node];
            }
            source_node++;
        }
        System.out.println("Missed edges:" + missed_edges);
        edges_final = e;
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
        //fill the array
        int n1 = 0;
        int n2 = 0;
        for (int j = 0; j < E; j++) { 
            do {
                pair = this.chooseEdge(1, 1, N + 1, N + 1, a, b, c, d);
                n1 = (int) pair[0];
                n2 = (int) pair[1];
            } while (array[n1 - 1] >= N - 1 || array[n2 - 1] >= N - 1);
            array[n1 - 1] = array[n1 - 1] + 1;
            if (this.edge_type == UNDIRECTED) {
                array[n2 - 1] = array[n2 - 1] + 1;
            }
        }
        return array;
    }

    Comparator<Long> comparator = new Comparator<Long>() {

        @Override
        public int compare(Long o1, Long o2) {
            return o2.compareTo(o1);
        }
    };

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
