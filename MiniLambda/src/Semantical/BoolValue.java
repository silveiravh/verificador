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
public abstract class BoolValue extends Value<Boolean>{

    public BoolValue(int line) {
        super(line);
    }
    
    public abstract Boolean value();
    
}
