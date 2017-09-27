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
public class Variable extends Value<Value<?>>{
    private String name;
    private Value<?> value;

    public Variable(String name) {
        super(-1);
        this.name = name;
        this.value = null;
    }

    public void setValue(Value<?> value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public Value<?> value() {
        return value;
    }
}
