/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import java.util.ArrayList;
import static verificador.Verificador.S;
import static verificador.Verificador.visited;

/**
 *
 * @author silveira
 */
public class State {
    private String name;
    private boolean i;
    private boolean f;
    String word;
    
    public State(String name) {
        this.name = name;
        this.i = false;
        this.f = false;
        this.word = "";
    }
    
    public State(String name, String word, Boolean i, Boolean f) {
        this.name = name;
        this.word = word;
        this.i = i;
        this.i = f;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    public String getWord() {
        return word;
    }

    public boolean isI() {
        return i;
    }

    public boolean isF() {
        return f;
    }

    
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public static boolean isVisited(State state) {
        for(State s : visited) {
            if(s.getName().equals(state.getName()) &&
               s.getWord().equals(state.getWord()))
                return true;
        }
        return false;
    }
    
    public static void printStates(ArrayList<State> S) {
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
