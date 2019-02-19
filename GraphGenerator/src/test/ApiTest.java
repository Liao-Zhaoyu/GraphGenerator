package test;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import rmat.Edge;
import rmat.RMatArrayApi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class ApiTest {

    public static void main(String[] args) {
        int N = 10000;
        int T = 1; // Type of edges (0 = undirected, 1 = directed)
        RMatArrayApi rmat = new RMatArrayApi(N, T, 1);
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data.graphml"), "UTF-8"));
            writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.write("\n<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
            writer.write("\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            writer.write("\n\txsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
            writer.write("\n\thttp://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
            if (T == 1) {
                writer.write("\n\t<graph id=\"G\" edgedefault=\"directed\" >");
            } else {
                writer.write("\n\t<graph id=\"G\" edgedefault=\"undirected\" >");
            }
            String line;
            System.out.println("Writing nodes...");
            for (int n = 0; n < N; n++) {
                line = "\n\t<node id=\"" + n + "\" />";
                writer.write(line);
            }

            System.out.println("Writing edges");
            Edge edge = rmat.nextEdge();
            int e = 1;
            while (edge != null) {
                line = "\n\t<edge id=\"" + e + "\" source=\"" + edge.getSourceNode() + "\" target=\"" + edge.getTargetNode() + "\"/>";
                writer.write(line);
                e++;
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
