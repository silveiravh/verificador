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
public class WhileCommand extends Command{
    private BoolValue expr;
    private Command cmd;

    public WhileCommand(BoolValue expr, Command cmd, int line) {
        super(line);
        this.expr = expr;
        this.cmd = cmd;
    }

    @Override
    public void execute() {
        while(expr.value()){
            cmd.execute();
        }
    }
}
