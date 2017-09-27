/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucasmarioza
 */
public class AssignCommand extends Command{
    private List<Variable> vars;
    private Value<?> value;
    
    public AssignCommand(Value<?> value,int line){
        super(line);
        this.value = value;
        this.vars = new ArrayList<Variable>();
    }

    public void addVariable(Variable var){
        vars.add(var);
    }
    
    public void execute() {
    Value<?> value = (this.value instanceof Variable) ? ((Variable) this.value).value() : this.value;
 
    Value<?> newValue = null;
    if (value instanceof IntValue) {
      IntValue iv = (IntValue) value;
      newValue = new ConstIntValue(iv.value(), -1);
    } else if (value instanceof StringValue) {
      StringValue sv = (StringValue) value;
      newValue = new ConstStringValue(sv.value(), -1);
    } else if (value instanceof ArrayValue) {
      ArrayValue av = (ArrayValue) value;
      newValue = new ConstArrayValue(av.value());
    } else {
        System.out.println(this.line+": Atribuição não suportada");//gramática não suporta o valor especificado
        System.exit(1);
    }
 
    for (Variable v : this.vars) {
      v.setValue(newValue);
    }
}
}
