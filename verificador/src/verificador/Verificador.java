/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import semantical.Letter;
import static semantical.Letter.printLetters;
import semantical.State;
import static semantical.State.printStates;
import semantical.Transition;
import lexical.LexicalAnalysis;
import static semantical.Transition.isVisited;
import syntatical.SyntaticalAnalysis;
import static semantical.Transition.printTransitions;

/**
 *
 * @author silveira
 */
public class Verificador {
    public static ArrayList<State> S = new ArrayList<State>();
    public static ArrayList<Letter> L = new ArrayList<Letter>();
    public static ArrayList<Transition> T = new ArrayList<Transition>();
    public static ArrayList<State> I = new ArrayList<State>();
    public static ArrayList<State> F = new ArrayList<State>();
    
    private static Stack<Transition> path = new Stack<Transition>();
    
    protected static String word="";
    private static State currentState;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java verificador [concat.al]");
            return;
        }

        try {
            LexicalAnalysis l = new LexicalAnalysis(args[0]);
            SyntaticalAnalysis s = new SyntaticalAnalysis(l);
            s.procAf();
            printStates();
            printLetters();
            printTransitions();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        System.out.println("");
        
        Scanner input = new Scanner(System.in);
        
        while(true) {
            word = input.nextLine();
            recognize(word);
        }
    }
    
    public static void recognize(String word) {
        ArrayList<State> inicials = getInicials();
        
        for(State i : inicials) {
            currentState = i;
            if(findPath(word)) {
                System.out.println("Sim");
            }
            else {
                System.out.println("Não");
            }
        }
    }
    
    static Stack<Transition> visited = new Stack<Transition>();
    static Stack<Transition> possibilities = new Stack<Transition>();
    
    public static boolean findPath(String w){
        
        
        
        ArrayList<Transition> lambdaTransitions = getLambdaTransitions(currentState, T);
        for(Transition t : lambdaTransitions) {
            if(!visited.contains(t))
                possibilities.push(t);
        }
        ArrayList<Transition> letterTransitions = getTransitions(currentState, getNextLetter(word), T);
        for(Transition t : letterTransitions) {
           possibilities.push(t);
        }
        
        if(w.isEmpty() && currentState.isFinal()) {
            return true;
        }
        
        for(Transition t : possibilities) {
            Transition t1 = possibilities.pop();
            w = doTransition(t, w);
            if(w.isEmpty() || currentState.isFinal()) {
                return true;
            }
            
            else {
                
                if(!visited.contains(t)) {
                    visited.add(t);
                findPath(w);
            }
            }
        }
        
        return false;
    }
    
    public static ArrayList<State> getInicials() {
        ArrayList<State> inicials = new ArrayList<State>();
        
        for(State s : S) {
            if(s.isInicial()) {
                inicials.add(s);
            }
        }
        
        return inicials;
    }
    
    public static ArrayList<Transition> getLambdaTransitions(State s, ArrayList<Transition> T) {
        ArrayList<Transition> lambdaTransitions = new ArrayList<Transition>();
        
        for(Transition t : T) {
            if(t.from().getName().equals(s.getName()) && t.letter().getSymbol()=='#') {
                lambdaTransitions.add(t);
            }
        }
        
        return lambdaTransitions;
    }
    
    public static Letter getNextLetter(String word) {
        if(!word.isEmpty()) {
            Letter l = new Letter(word.charAt(0));
        
        return l;
        }
        return null;
    }
    
    public static ArrayList<Transition> getTransitions(State s, Letter l, ArrayList<Transition> T) {
        ArrayList<Transition> transitions = new ArrayList<Transition>();
        
        for(Transition t : T) {
            if(t.from().getName().equals(s.getName()) && t.letter().getSymbol()==l.getSymbol()) {
                transitions.add(t);
            }
        }
        
        return transitions;
    }
    
    public static String doTransition(Transition t, String w) {
        if(t.letter().getSymbol()!='#')
            w = forward(w);
        currentState = t.to();
        return w;
    }
    
    public static String forward(String s) {
        s = s.substring(1, s.length());
        return s;
    }
    
    public static State undoTransition(Transition t) {
        backward(word, t.letter());
        return t.from();
    }
    
    public static String backward(String s, Letter l) {
        s = l.getSymbol() + s;
        return s;
    }
}
