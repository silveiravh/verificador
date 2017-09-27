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
public class CompareBoolValue extends BoolValue{
    private RelOp op;
    private Value<?> left;
    private Value<?> right;

    public CompareBoolValue(RelOp op, Value<?> left, Value<?> right, int line) {
        super(line);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean value() {
        Value<?> left = this.left instanceof Variable ? ((Variable) this.left).value() : this.left;
        Value<?> right = this.right instanceof Variable ? ((Variable) this.right).value() : this.right;
        switch(op){
            case Equal:
                return left.value() == right.value();
            case GreaterEqual:
                if(left instanceof IntValue && right instanceof IntValue){
                    return ((IntValue)left).value() >= ((IntValue)right).value();
                }else{
                    System.out.println(this.line+": Operação lógica inválida");
                    System.exit(1);
                }
            case LowerEqual:
                if(left instanceof IntValue && right instanceof IntValue){
                    return ((IntValue)left).value() <= ((IntValue)right).value();
                }else{
                    System.out.println(this.line+": Operação lógica inválida");
                    System.exit(1);
                }
            case GreaterThan:
                if(left instanceof IntValue && right instanceof IntValue){
                    return ((IntValue)left).value() > ((IntValue)right).value();
                }else{
                    System.out.println(this.line+": Operação lógica inválida");
                    System.exit(1);
                }
            case LowerThan:
                if(left instanceof IntValue && right instanceof IntValue){
                    return ((IntValue)left).value() < ((IntValue)right).value();
                }else{
                    System.out.println(this.line+": Operação lógica inválida");
                    System.exit(1);
                }
            case NotEqual:
                return left.value() != right.value();
        }
        return false;
    }
}
