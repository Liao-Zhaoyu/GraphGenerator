package rmat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author renzo angles
 */

public class RMat {

    private boolean directed_graph;
    private long nodes;
    private long edges;
    private long[] distribution;
    private int source_node;
    private int target_node;
    private long degree;
    //R-MAT Probabilities
    double alpha = 0.6;
    double beta = 0.15;
    double gamma = 0.15;
    double delta = 0.10;
    //opc1: 0.55 0.15 0.15 0.15 ok
    //opc2: 0.55 0.15 0.1 0.2 anomalia
    private double offset1;
    private double offset2;
    private double offset3;
    private double offset4;
    private BinEdge binEdge ;
    int seed = 80808080;
    Random rand;

    public RMat(long nodes_number) {
        this.init(nodes_number, true, true);
    }

    public RMat(long nodes_number, boolean _directed_graph) {
        this.init(nodes_number, _directed_graph, true);

    }

    public RMat(long nodes_number, boolean _directed_graph, boolean powerlaw) {
        this.init(nodes_number, _directed_graph, powerlaw);
    }

    private void init(long nodes_number, boolean _directed_graph, boolean powerlaw) {
        rand = new Random(nodes_number);
        this.directed_graph = _directed_graph;
        if (powerlaw == false) {
            this.alpha = 0.25;
            this.beta = 0.25;
            this.gamma = 0.25;
            this.delta = 0.25;
        }
        nodes = nodes_number;
        edges = (int) (Math.log(nodes) * nodes); 
        distribution = this.makeDistribution((int) nodes, edges, directed_graph);
        source_node = -1;
        degree = 0;
    }

    public long nodesNumber() {
        return this.nodes;
    }

    public long edgesNumber() {
        return this.edges;
    }

    //método que construye una distribucion de degrees simulando R-Mat
    private long[] makeDistribution(int N, long E, boolean directed_graph) {
        float eOffset=0;
        long[] array = new long[N];
        long pair[];

        for (int i = 0; i < N; i++) {
            array[i] = 0; //inicializa cada degree en 0
        }

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
            if (directed_graph == false) {
                array[n2 - 1] = array[n2 - 1] + 1;
            }
        }

        //a los elementos con 0, les asigno 1 
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = 1;
                eOffset++;
            }
        }

        /*
         //sorting the distributions in ascending order
         //lo comente ya que extrañamente no funciona bien
         ArrayList<Integer> list = new ArrayList<Integer>();
         for (int i = 0; i < array.length; i++) {
         list.add((int) array[i]);
         }

         Collections.sort(list);
         long[] sorted = new long[N];
         int j = N - 1;
         Iterator<Integer> it = list.iterator();
         while (it.hasNext()) {
         sorted[j] = it.next();
         j--;
         }
         return sorted;
         */
        
        Long arr[] = new Long[array.length];
        int i=0;
        for(long temp:array){
             arr[i++] = temp;
         }

         Arrays.sort(arr,comparator);
         array= new long[arr.length];
         i=0;
        for(long temp:arr){
             array[i++] = temp;
        }
        if (!directed_graph) 
            eOffset=(float) Math.ceil(eOffset/2);

        edges+=eOffset;
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
            //System.out.println("EDGE:" + x1 + "-" + y1);
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

    public long[] getDistribution() {
        return distribution;
    }

    public void printDistribution() {
        for (int i = 0; i < distribution.length; i++) {
            System.out.println(distribution[i]);
        }
    }

    public Edge nextEdge() {
        Edge edge;
        
        while (true) {
            if (source_node >= nodes) {
                return null;
            }
            while (degree <= 0) {
                source_node++;
                if (source_node >= nodes) {
                    return null;
                }
                degree = distribution[source_node];  //obtengo el degree del nodo
                target_node = source_node;
                binEdge = new BinEdge(distribution.length,source_node);
                //System.out.println("node:" + source_node + "  degree:" + degree);
            }
            if (directed_graph == true) {
                //si es un grafo dirigido, el target node es cualquiera que esté adelante en la lista
                target_node=binEdge.nextCell();
                if (target_node == -1) {
                    //System.out.println("falto" + degree);
                    continue;
                }
//                if (target_node >= nodes) {
//                    //binEdge = new BinEdge(distribution.length-source_node, source_node);
//                    target_node = 0;
//                }

            } else {
                //si es un grafo no dirigido, debo buscar un nodo destino con degree > 0
                target_node=binEdge.nextCell();
                while (target_node !=-1) {
                    if (distribution[target_node] <= 0) {
                         target_node=binEdge.nextCell();
                    } else {
                        break;
                    }
                }
                // si no encuentro un nodo destino con degree > 0, busco el siguiente nodo
                if (target_node == -1) {
                    //System.out.println("falto" + degree);
                    degree = 0;
                    continue;
                }
                //disminuyo el degree del target node
                distribution[target_node] = distribution[target_node] - 1;
            }
            edge = new Edge(source_node + 1, target_node + 1);
            distribution[source_node] = distribution[source_node] - 1;
            degree--;
            return edge;
        }
    } 
}
