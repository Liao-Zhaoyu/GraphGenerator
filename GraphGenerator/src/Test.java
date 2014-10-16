
import rmat.RMat;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class Test {

    public static void main(String[] args) {
        int N = 1000;
        int edge_type = 1;
        int dist_type = 1; 


        RMat rmat = new RMat(N, true, true);
        long[] dist = rmat.getDistribution();
        long[] degrees = new long[N];
        int degree;
        for (int i = 0; i < dist.length; i++) {
            degree = (int) dist[i];
            degrees[degree] = degrees[degree] + 1;
        }

        System.out.println("Number of nodes: " + rmat.nodesNumber());
        System.out.println("Number of edges: " + rmat.edgesNumber());

        try {
            Writer writer;
            String line;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dist.dat"), "UTF-8"));
            long min_degree = N;
            long max_degree = 0;
            long sum_degrees = 0;
            long min_nodes = N;
            long max_nodes = 0;
            long counter = 0;
            for (int j = 0; j < N; j++) {
                if (degrees[j] > 0) {
                    line = j + " " + degrees[j] + "\n";
                    sum_degrees = sum_degrees + j;
                    counter++;
                    writer.write(line);
                    if (j < min_degree) {
                        min_degree = j;
                    }
                    if (j > max_degree) {
                        max_degree = j;
                    }
                    if (degrees[j] < min_nodes) {
                        min_nodes = degrees[j];
                    }
                    if (degrees[j] > max_nodes) {
                        max_nodes = degrees[j];
                    }
                }
            }
            writer.close();

            System.out.println("Min degree:" + min_degree);
            System.out.println("Max degree:" + max_degree);
            double avg_degree = sum_degrees / counter;
            System.out.println("Avg degree:" + avg_degree);

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dist.gp"), "UTF-8"));
            writer.write("reset \n");
            //writer.write("set terminal latex \n");
            //writer.write("set output \"dist.tex\" \n");
            //writer.write("set size 5/5., 4/3. \n");
            writer.write("set size square \n");
            writer.write("set term png size 1000, 1000 \n");
            writer.write("set output \"dist.png\" \n");
            writer.write("set title \"Degrees Distribution\" \n");
            writer.write("set xlabel \"Degree\" \n");
            writer.write("set ylabel \"Nodes\" \n");
            //writer.write("set logscale xy \n");
            writer.write("plot [0:" + max_degree + "] [0:" + max_nodes + "] \"dist.dat\" title \"-\" with points \n");
            writer.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        

    }
}
