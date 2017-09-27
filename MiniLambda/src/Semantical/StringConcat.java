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
public class StringConcat extends StringValue{
    private Value<?> left;
    private Value<?> right;

    public StringConcat(Value<?> left, Value<?> right, int line) {
        super(line);
        this.left = left;
        this.right = right;
    }
    
    
    @Override
    public String value() {
        Value<?> left = this.left instanceof Variable ? ((Variable) this.left).value() : this.left;
        Value<?> right = this.right instanceof Variable ? ((Variable) this.right).value() : this.right;
        if(left instanceof ArrayValue || right instanceof ArrayValue || left instanceof BoolValue || right instanceof BoolValue){
            System.out.println(this.line+": Concatenação não suportada");
            System.exit(1);
            
        }else{
            if(left instanceof IntValue){
                left = new ConstStringValue(""+left.value(),left.getLine());
            }
            if(right instanceof IntValue){
                right = new ConstStringValue(""+right.value(),right.getLine());
            }
            
            return ((StringValue)left).value()+((StringValue)right).value();
        }
        return null;
    }
    
}
