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
public class IfCommand extends Command{
    private BoolValue expr;
    private Command thenExpr;
    private Command elseExpr;

    public IfCommand(BoolValue expr, Command thenExpr, Command elseExpr, int line) {
        super(line);
        this.expr = expr;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    public IfCommand(BoolValue expr, Command thenExpr, int line) {
        super(line);
        this.expr = expr;
        this.thenExpr = thenExpr;
        this.elseExpr = null;
    }

    
    
    @Override
    public void execute() {
        if(expr.value()){
            thenExpr.execute();
        }else if(elseExpr != null){
            elseExpr.execute();
        }
    }
    
}
