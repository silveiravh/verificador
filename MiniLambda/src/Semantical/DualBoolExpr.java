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
public class DualBoolExpr extends BoolValue{
    private BoolOp op;
    private BoolValue left;
    private BoolValue right;

    public DualBoolExpr(BoolOp op, BoolValue left, BoolValue right, int line) {
        super(line);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean value() {
        switch (op) {
            case And:
                return left.value()&&right.value();
            case Or:
                return left.value()||right.value();
            default:
                System.out.println(this.line+": Operação lógica inválida");
                System.exit(1);
                break;
        }
        return null;
    }
}
