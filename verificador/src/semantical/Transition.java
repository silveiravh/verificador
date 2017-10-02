/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import static syntatical.Verificador.T;

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
    
    public static void printTransitions() {
        System.out.println("Transições");
        for(Transition t : T) {
            System.out.println("from: "+t.from.getName()+" to "+t.to.getName()+" consuming "+t.letter.getSymbol());
        }
    }
}
