/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import java.util.ArrayList;
import static verificador.Verificador.L;

/**
 *
 * @author silveira
 */
public class Letter {
    private char symbol;
    
    public Letter(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    
    public static void printLetters(ArrayList<Letter> L) {
        System.out.println("Letras: ");
        for(Letter l : L) {
            System.out.println("Symbol: "+l.symbol);
        }
        System.out.println("");
    }
}
