/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

import java.util.Scanner;

/**
 *
 * @author lucasmarioza
 */
public class LoadIntValue extends IntValue{
    private Value<?> text;

    public LoadIntValue(Value<?> text, int line) {
        super(line);
        this.text = text;
    }

    @Override
    public Integer value() {
        PrintCommand pc = new PrintCommand(this.text,false,this.getLine());
        pc.execute();
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
    }
    
}
