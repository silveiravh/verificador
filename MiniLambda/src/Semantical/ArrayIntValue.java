/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

/**
 *
 * @author lucasmarioza
 */
public abstract class ArrayIntValue extends IntValue{
    protected Value<?> array;
    
    public abstract Integer value();

    public ArrayIntValue(Value<?> array, int line) {
        super(line);
        this.array = array;
    }
}
