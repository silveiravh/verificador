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
import static semantical.State.isVisited;
import static semantical.State.printStates;
import semantical.Transition;
import lexical.LexicalAnalysis;
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
    
   
    public static ArrayList<State> visited = new ArrayList<State>();
    
    public static boolean hasPath;
    
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
            printStates(S);
            printLetters(L);
            printTransitions(T);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        System.out.println("");
        
        Scanner input = new Scanner(System.in);
        
        
        
        
        while(true) {
            String word = input.nextLine();
            hasPath = false;
            recognize(word);
        }
        
    }
    
    public static void recognize(String word) {
        ArrayList<State> inicials = getInicials();
        
        for(State i : inicials) {
            i.setWord(word);
            findPath(i);
            if(hasPath) {
                System.out.println("Sim");
                return;
            }
        }
        
        System.out.println("Não");
        return;
    }
    
    public static void findPath(State s){
        System.out.println(s.getName());
        System.out.println(s.getWord());
        Stack<State> possibilities = new Stack<State>();
        
        ArrayList<Transition> lambdaTransitions = new ArrayList<Transition>();
        ArrayList<Transition> letterTransitions = new ArrayList<Transition>();
        
        lambdaTransitions = getLambdaTransitions(s, T);
        for(Transition t : lambdaTransitions) {
            doTransition(t, s);
            if(!isVisited(s)) {        
                possibilities.push(s);
            }
            undoTransition(t, s);
        }
        
        System.out.println("Possibilidades");
        for(State t : possibilities) {
            System.out.println(s.getName());
            System.out.println(s.getWord());
        }
        
        Letter l = getNextLetter(s.getWord());
        if(l!=null) {
            letterTransitions = getTransitions(s, l , T);
        }
        for(Transition t : letterTransitions) {
            doTransition(t, s);
            if(!isVisited(s)) {  
                possibilities.push(s);
            }
            undoTransition(t, s);
        }
        
        /*
        System.out.println("State: "+s.getName());
        for(Transition t : possibilities) {
            t.printTransition();
        }
        */
        
        while(!possibilities.empty() && !hasPath) {
            s = possibilities.pop();
            System.out.println(s.getWord());
            visited.add(s);
            if(s.getWord().isEmpty() && s.isFinal()) {
                hasPath = true;
                return;
            }
        }
        
        return;
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
    
    public static void doTransition(Transition t, State s) {
        if(t.letter().getSymbol()!='#')
            s.setWord(forward(s.getWord()));
        s.setName(t.to().getName());
    }
    
    public static void undoTransition(Transition t, State s) {
        if(t.letter().getSymbol()!='#')
            s.setWord(backward(s.getWord(), t.letter()));
        s.setName(t.from().getName());
    }
    
    public static String forward(String s) {
        s = s.substring(1, s.length());
        return s;
    }
    
    public static String backward(String s, Letter l) {
        return (l.getSymbol() + s);
    }
}
