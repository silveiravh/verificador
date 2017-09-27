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
public class AddArrayValue extends ArrayValue{
    private Value<?> array;   
    private Value<?> value;   

    public AddArrayValue(Value<?> array, Value<?> value, int line) {
        super(line);
        this.array = array;
        this.value = value;
    }

    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        Value<?> value = this.value instanceof Variable ? ((Variable) this.value).value() : this.value;
        if(array instanceof ArrayValue){
            if(value instanceof ArrayValue)
                return ((ArrayValue)array).value().add(((ArrayValue)value).value());
            if(value instanceof IntValue)
                return ((ArrayValue)array).value().add(((IntValue)value).value());
        }else{
            System.out.println(this.line+"Impossível concatenar realizar concatenação no vetor");     
            System.exit(1);
        }
        return null;        
    }
}
