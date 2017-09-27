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
public class DualIntExpr extends IntValue{
    private IntOp op;
    //private Value<?> left;
    //private Value<?> right;
    private Value<?> left;
    private Value<?> right;

    public DualIntExpr(IntOp op, Value<?> left, Value<?> right, int line) {
        super(line);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer value() {
        Value<?> left = this.left instanceof Variable ? ((Variable) this.left).value() : this.left;
        Value<?> right = this.right instanceof Variable ? ((Variable) this.right).value() : this.right;
        if(left instanceof IntValue && right instanceof IntValue){
            IntValue l = (IntValue)left;
            IntValue r = (IntValue)right;
            switch (op) {
                case Add:
                    return l.value()+r.value();
                case Sub:
                    return l.value()-r.value();
                case Div:
                    return l.value()/r.value();
                case Mul:
                    return l.value()*r.value();
                case Mod:
                    return l.value()%r.value();
                default:
                    break;
            }
        }else{
            System.out.println(this.line+": Operação de inteiros inválida");
            System.exit(1);
        }
        return null;
    }
}
