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
public class ConstStringValue extends StringValue{
    private String text;
    public ConstStringValue(String text, int line) {
        super(line);
        this.text = text;
    }
    public String value() {
        return text;
    }
}
