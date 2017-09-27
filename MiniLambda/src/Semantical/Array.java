/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

import java.util.Arrays;

/**
 *
 * @author lucasmarioza
 */
public class Array {
    private int size;
    private int array[];

    public Array(int size) {
        this.size = size;
        array = new int[size];
    }
    
    public int at(int index){
        return array[index];
    }
    
    public int size(){
        return size;
    }
    
    public void show(){
        System.out.print("[");
        for(int i =0;i<size-1;i++){
            System.out.print(array[i]+", ");
        }
        System.out.print(array[size-1]+"]");
    }
    
    public void set(int index,int value){
        array[index]=value;
    }  
    
    public Array sort(){
        Array a = new Array(this.size);
        a.array = Arrays.copyOf(array, size);
        Arrays.sort(a.array);
        return a;
    }
    
    public Array add(int value){
        int newSize = this.size+1;
        Array a = new Array(newSize);
        a.array = Arrays.copyOf(this.array,newSize);
        a.array[size] = value;
        return a;
    }
    
    public Array add(Array array){
        int newSize = this.size+array.size;
        Array a = new Array(newSize);
        a.array = Arrays.copyOf(this.array,newSize);
        System.arraycopy(array.array, 0, a.array, size, array.size);
        return a;
    }

    @Override
    public String toString() {
        return "Array{" + "size=" + size + ", array=" + array + '}';
    }
    

}
