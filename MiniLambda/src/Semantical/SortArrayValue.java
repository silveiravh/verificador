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
public class SortArrayValue extends ArrayValue{
    private Value<?> array;   

    public SortArrayValue(Value<?> array, int line) {
        super(line);
        this.array = array;
    }

    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue){
            return ((ArrayValue)array).value().sort();
        }else{
            System.out.println(this.line+"Impossível ordenar estrutura");
            System.exit(1);
        }
        return null;
    }

}
