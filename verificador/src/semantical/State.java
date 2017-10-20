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
    
    public State(String name, boolean i, boolean f) {
        this.name = name;
        this.i = i;
        this.f = f;
        this.word = "";
    }
    
    public State(String name, String word, Boolean i, Boolean f) {
        this.name = name;
        this.word = word;
        this.i = i;
        this.f = f;
    }

    public void setI(boolean i) {
        this.i = i;
    }

    public void setF(boolean f) {
        this.f = f;
    }

    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public static boolean isInicial(String stateName) {
        for(State s : S) {
            if(s.getName().equals(stateName)) {
                return s.isI();
            }
        }
        return false;
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
    
    public static boolean isFinal(String stateName) {
        for(State s : S) {
            if(s.getName().equals(stateName)) {
                return s.isFinal();
            }
        }
        return false;
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
        return this.i;
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
    
    public int getFinal() {
        if(this.f)
            return 1;
        else
            return 0;
    }
    
    public static void printStates(ArrayList<State> S) {
        System.out.println("Estados:");
        for(State s : S) {
            System.out.print("Nome: "+s.name);
            if(s.i) {
                System.out.print(" inicial");
            }
            if(s.f) {
                System.out.print(" final");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
