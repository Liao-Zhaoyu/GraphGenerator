
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import rmat.Edge;
import rmat.RMat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renzo
 */
public class Main {

    private static void printInfo() {
        System.out.println("GraphGenerator is an API and java application for generating graphs using the R-Mat graph data generation method.");
        System.out.println("The java application (GraphGenerator.jar) takes three (sorted) parameters: number of nodes (integer), type of edges (0 = undirected, 1 = directed) and statistical distribution of edges (0 = normal, 1 = powerlaw).");
        System.out.println("The resulting graph is returned as a GraphML file.");        
    }

    public static void main(String[] args) {
        int N = 1000;
        int edge_type = 0;
        int dist_type = 0; 
        if (args.length == 0) {
            printInfo();
            return;
        }

        try {
            N = Integer.parseInt(args[0]);
            edge_type = Integer.parseInt(args[1]);
            dist_type = Integer.parseInt(args[2]);
            
        } catch (Exception ex) {
            System.out.println("Error: Invalid parameters");
            System.out.println(ex);
            return;
        }

        
        boolean directed = true;
        if(edge_type == 0){
            directed = false; 
        }
        boolean powerlaw = true;
        if(dist_type == 0){
            powerlaw = false;
        }
        
        RMat rmat = new RMat(N, directed, powerlaw);
        System.out.println("Number of nodes: " + rmat.nodesNumber());
        System.out.println("Number of edges: " + rmat.edgesNumber());
        
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data.graphml"), "UTF-8"));
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.write("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
            writer.write("\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            writer.write("\n\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
            writer.write("\n\thttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
            if(directed == true){
                writer.write("\n\t<graph id=\"G\" edgedefault=\"directed\" >");
            }else{
                writer.write("\n\t<graph id=\"G\" edgedefault=\"undirected\" >");
            }
            String line;
            System.out.println("Writing nodes...");
            for (int n = 1; n <= N; n++) {
                line = "\n\t<node id=\"" + n + "\" />";
                writer.write(line);
            }

            System.out.println("Writing edges");
            Edge edge = rmat.nextEdge();
            int e = 1;
            while (edge != null) {
                e++;
                line = "\n\t<edge id=\"" + e + "\" source=\"" + edge.getSourceNode() + "\" target=\"" + edge.getTargetNode() + "\"/>";
                writer.write(line);
                //dirigido
                e++;
                line = "\n\t<edge id=\"" + e + "\" source=\"" + edge.getTargetNode() + "\" target=\"" + edge.getSourceNode() + "\"/>";
                writer.write(line);
                edge = rmat.nextEdge();
            }

            writer.write("\n\t</graph>");
            writer.write("\n</graphml>");
            writer.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("OK");

        
    }

}
