/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import java.util.ArrayList;
import static verificador.Verificador.T;
import static verificador.Verificador.visited;

/**
 *
 * @author silveira
 */
public class Transition {
    private State from;
    private Letter letter;
    private State to;
    
    public Transition(State from, Letter letter, State to) {
        this.from = from;
        this.letter = letter;
        this.to = to;
    }

    public State from() {
        return from;
    }

    public Letter letter() {
        return letter;
    }

    public State to() {
        return to;
    }
    
    public static void printTransitions(ArrayList<Transition> T) {
        System.out.println("Transitions:");
        for(Transition t : T) {
            System.out.println("from "+"\""+t.from.getName()+"\""+" to "+"\""+t.to.getName()+"\""+" consuming "+"\""+t.letter.getSymbol()+"\"");
        }
    }
}
