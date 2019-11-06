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
public class PrimitiveArray extends Array {
    
    long[] array;
    
    public PrimitiveArray(int array_size){
        this.size = array_size;
        array = new long[(int)this.size];
    }

    @Override
    public long getElement(long index){
        if(index < size){
            return array[(int)index];
        }
        throw new RuntimeException("Invalid index");
    }
    
    @Override
    public boolean setElement(long index, long value){
        if(index < size){
            array[(int)index] = value;
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
            array[(int)index] = array[(int)index] + value;
            return array[(int)index];
        }
        throw new RuntimeException("Invalid index");
    }
    
    @Override
    long decreaseElement(long index, long value){
        if(index < size){
            array[(int)index] = array[(int)index] - value;
            return array[(int)index];
        }
        throw new RuntimeException("Invalid index");
    }    
    
}
