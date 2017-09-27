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
public class ShowArrayValue extends ArrayValue{
    private Value<?> array;

    public ShowArrayValue(Value<?> array, int line) {
        super(line);
        this.array = array;
    }
    
    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue){
            Array ar = ((ArrayValue)array).value();
            ar.show();
            return ar;
        }else{
            System.out.println(line+"Impossível imprimir o vetor");
            System.exit(1);
        }
        return null;
    }
    
}
