/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

/**
 *
 * @author aluno
 */
public class PrintCommand extends Command{
    private Value<?> value;
    private boolean newLine;
    public PrintCommand(Value<?> value,boolean newLine, int line){
        super(line);
        this.value = value;
        this.newLine = newLine;
    }

    @Override
    public void execute() {
        Value<?> value = this.value instanceof Variable ? ((Variable) this.value).value() : this.value;
        String text ="";
        if(value instanceof StringValue){
            StringValue sv = (StringValue) value;
            text = sv.value();
        }else if(value instanceof IntValue){
            IntValue iv = (IntValue) value;
            text = "" + iv.value();
        }else if(value instanceof ArrayValue){
            text = ((ArrayValue)value).value().toString();
        }else{
            System.out.println(this.line+": Erro semântico");
            System.exit(1);
        }
        
        System.out.print(text);
        if(newLine)
            System.out.println();
    }
}
