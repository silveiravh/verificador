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
public class EachArrayValue extends ArrayValue{
    private Value<?> array;
    private Variable var;
    private Command cmd;

    public EachArrayValue(Value<?> array, Variable var, Command cmd, int line) {
        super(line);
        this.array = array;
        this.var = var;
        this.cmd = cmd;
    }
    
    @Override
    public Array value() {
        Value<?> array = this.array instanceof Variable ? ((Variable) this.array).value() : this.array;
        if(array instanceof ArrayValue){
            Array origArray = ((ArrayValue)array).value();
            int line = this.getLine();
            for(int i =0;i<origArray.size();i++){
                int value = origArray.at(i);
                var.setValue(new ConstIntValue(value,line));
                cmd.execute();
            }
            return origArray;
        }else{
            System.out.println(this.line+"Estrutura inválida para o comando Each");
            System.exit(1);
        }
        return null;
    }
    
    
}
