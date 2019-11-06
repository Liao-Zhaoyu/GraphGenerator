/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmat;

/**
 *
 * @author renzo
 */
abstract class Array {
    
    protected long size = 0;
    
    public void Array(){
    }
    
    abstract long getElement(long index);
    abstract boolean setElement(long index, long value);
    abstract long increaseElement(long index, long value);
    abstract long decreaseElement(long index, long value);    
    abstract long size();    
    
}
