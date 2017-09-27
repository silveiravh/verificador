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
public class SetArrayValue extends ArrayValue{
    private Value<?> array;
    private Value<?> index;
    private Value<?> value;

    public SetArrayValue(Value<?> array, Value<?> index, Value<?> value, int line) {
        super(line);
        this.array = array;
        this.index = index;
        this.value = value;
    }

    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue && index instanceof IntValue && value instanceof IntValue){
            ((ArrayValue)array).value().set(((IntValue)index).value(), ((IntValue)value).value());
            return ((ArrayValue)array).value();
        }else{
            System.out.println(this.line+"Impossível setor valor no vetor");
            System.exit(1);
        }
        return null;
    }

}
