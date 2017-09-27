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
public class SizeArrayIntValue extends ArrayIntValue{

    public SizeArrayIntValue(Value<?> array, int line) {
        super(array, line);
    }

    @Override
    public Integer value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue){
            return ((ArrayValue)array).value().size();
        }else{
            System.out.println(this.line+": Tipo não esperado");
            System.exit(1);
        }
        return null;
    }
    
}
