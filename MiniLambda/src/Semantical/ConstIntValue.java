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
public class ConstIntValue extends IntValue{
    private int value;

    public ConstIntValue(int value, int line) {
        super(line);
        this.value = value;
    }

    @Override
    public Integer value() {
        return value;
    }
}
