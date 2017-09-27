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
public class AtArrayIntValue extends ArrayIntValue{
    private Value<?> index;

    public AtArrayIntValue(Value<?> array, Value<?> index, int line) {
        super(array, line);
        this.index = index;
    }

    @Override
    public Integer value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue && index instanceof IntValue){
            return ((ArrayValue)array).value().at(((IntValue)index).value());
        }else{
            System.out.println(this.line+": Tipo não esperado");
            System.exit(1);
        }
        return null;
    }
    
}
