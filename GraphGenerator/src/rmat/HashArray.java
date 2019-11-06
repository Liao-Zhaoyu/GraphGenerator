/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmat;

import java.util.HashMap;

/**
 *
 * @author renzo
 */
public class HashArray extends Array {
    
    HashMap<Long,Long> array;
    
    public HashArray(long array_size){
        this.size = array_size;
        array = new HashMap<>();
    }

    @Override
    public long getElement(long index){
        if(index < size){
            return array.get(index);
        }
        throw new RuntimeException("Invalid index");
    }
    
    @Override
    public boolean setElement(long index, long value){
        if(index < size){
            array.put(index, value);
            return true;
        }
        throw new RuntimeException("Invalid index");
    }
    
    @Override
    public long size(){
        return size;
    }    
    
    @Override
    long increaseElement(long index, long value){
        if(index < size){
            long nvalue = array.get(index) + value;
            array.put(index, nvalue);
            return nvalue;
        }
        throw new RuntimeException("Invalid index");
    }
    
    @Override
    long decreaseElement(long index, long value){
        if(index < size){
            long nvalue = array.get(index) - value;
            array.put(index, nvalue);
            return nvalue;
        }
        throw new RuntimeException("Invalid index");
    }    
    
}
