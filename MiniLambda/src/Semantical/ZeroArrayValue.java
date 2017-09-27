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
public class ZeroArrayValue extends ArrayValue{
    private Value<?> size;

    public ZeroArrayValue(Value<?> size, int line) {
        super(line);
        this.size = size;
    }  
    
    @Override
    public Array value() {
        Value<?> size = this.size instanceof Variable ? ((Variable) this.size).value() : this.size;
        if(size instanceof IntValue){
            Array array = new Array(((IntValue)size).value());
            for(int i = 0;i<array.size();i++){
                array.set(i,0);
            }
            return array;
        }else{
            System.out.println(this.line+"Impossível criar vetor de zeros");
            System.exit(1);
        }
        return null;
    }
    
}
