/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import static verificador.Verificador.S;

/**
 *
 * @author silveira
 */
public class State {
    private String name;
    private Boolean i;
    private Boolean f;
    
    public State(String name) {
        this.name = name;
        this.i = false;
        this.f = false;
    }

    public String getName() {
        return name;
    }
    
    public boolean isInicial() {
        return this.i;
    }
    
    public static void setInicial(State state) {
        for(State s : S) {
            if(s.name.equals(state.name)) {
                s.i = true;
            }
        }
    }
    
    public boolean isFinal() {
        return this.f;
    }
    
    public static void setFinal(State state) {
        for(State s : S) {
            if(s.name.equals(state.name)) {
                s.f = true;
            }
        }
    }
    
    public static void printStates() {
        System.out.println("Estados:");
        for(State s : S) {
            System.out.print("Nome: "+s.name);
            if(s.i) {
                System.out.print(" inicial");
            }
            else if(s.f) {
                System.out.print(" final");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
