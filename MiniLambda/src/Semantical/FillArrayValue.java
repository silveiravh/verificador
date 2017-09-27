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
public class FillArrayValue extends ArrayValue{
    private Value<?> size;
    private Value<?> value;

    public FillArrayValue(Value<?> size, Value<?> value, int line) {
        super(line);
        this.size = size;
        this.value = value;
    }    
    
    @Override
    public Array value() {
        Value<?> size = this.size instanceof Variable ? ((Variable) this.size).value() : this.size;
        Value<?> value = this.value instanceof Variable ? ((Variable) this.value).value() : this.value;
        if(size instanceof IntValue && size instanceof IntValue){
            Array array = new Array(((IntValue)size).value());
            for(int i = 0;i<array.size();i++){
                array.set(i,((IntValue)value).value());
            }
            return array;
        }else{
            System.out.println(this.line+"Impossível a estrutura vetor com valor especificado");
            System.exit(1);
        }
        return null;
    }
       
}
