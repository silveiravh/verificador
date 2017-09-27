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
public class RemoveArrayValue extends ArrayValue{
    private Value<?> array;
    private Variable var;
    private BoolValue cond;

    public RemoveArrayValue(Value<?> array, Variable var, BoolValue cond, int line) {
        super(line);
        this.array = array;
        this.var = var;
        this.cond = cond;
    }

    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue){
            Array origArray = ((ArrayValue)array).value();
            int line = this.getLine();
            Array newArray= new Array(0);
            for(int i =0;i<origArray.size();i++){
                int value = origArray.at(i);
                var.setValue(new ConstIntValue(value,line));
                if(!cond.value()){
                    newArray=newArray.add(value);
                }
            }
            return newArray;
        }else{
            System.out.println(this.line+"Estrutura não esperada para o comando Remove");
            System.exit(1);
        }
        return null;
    }
}
