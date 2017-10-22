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
    String word;
    private boolean i;
    private boolean f;
    
    public State(String name) {
        this.name = name;
        this.word = "";
        this.i = false;
        this.f = false;
    }  
    
    public State(String name, String word, boolean i, boolean f) {
        this.name = name;
        this.word = word;
        this.i = i;
        this.f = f;
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
    
    public static boolean isInicial(String stateName) {
        for(State s : S) {
            if(s.getName().equals(stateName)) {
                return s.isInicial();
            }
        }
        return false;
    }
    
    public void setInicial(boolean i) {
        this.i = i;
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
    
    public void setFinal(boolean f) {
        this.f = f;
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
        System.out.println("States:");
        for(State s : S) {
            System.out.print("Name: "+"\""+s.name+"\",");
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
