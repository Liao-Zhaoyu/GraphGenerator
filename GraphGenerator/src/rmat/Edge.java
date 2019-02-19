package rmat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author renzo
 */
public class Edge {
    
    private long source;
    private long target;
    
    public Edge(long source_node, long target_node){
        this.source = source_node;
        this.target = target_node;
    }
    
    public long getSourceNode(){
        return source; 
    }
    
    public long getTargetNode(){
        return target;
    }
    
}
