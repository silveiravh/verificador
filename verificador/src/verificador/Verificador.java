/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import semantical.Letter;
import static semantical.Letter.printLetters;
import semantical.State;
import static semantical.State.isVisited;
import static semantical.State.printStates;
import semantical.Transition;
import lexical.LexicalAnalysis;
import static semantical.State.isFinal;
import static semantical.State.isInicial;
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
    
    public static ArrayList<State> visited;
    
    public static boolean hasPath;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
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
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        for(Transition t : T) {
            if(isInicial(t.from().getName())) {
                t.from().setInicial(true);
            }
            if(isFinal(t.from().getName())) {
                t.from().setFinal(true);
            }
            if(isInicial(t.to().getName())) {
                t.to().setInicial(true);
            }
            if(isFinal(t.to().getName())) {
                t.to().setFinal(true);
            }
        }
        
        
        String word;
        
        while((word = br.readLine()) != null) {
            
            visited = new ArrayList<State>();
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
        State currentState = new State(s.getName(), s.getWord(), s.isInicial(), s.isFinal()); 
        System.out.print("Current state: "+currentState.getName()+" "+currentState.getWord());
        if(currentState.isInicial()) {
            System.out.print(" inicial");
        }
        if(currentState.isFinal()) {
            System.out.print(" final");
        }
        
        System.out.println("");
        
        Stack<State> possibilities = new Stack<State>();
        
        ArrayList<Transition> lambdaTransitions = new ArrayList<Transition>();
        ArrayList<Transition> letterTransitions = new ArrayList<Transition>();
        
        Letter l = getNextLetter(currentState.getWord());
        
        lambdaTransitions = getLambdaTransitions(currentState, T);
        for(Transition t : lambdaTransitions) {
            if(l!=null) {
                doTransition(t, currentState, l.getSymbol());
            }
            else {
                doTransition(t, currentState);
            }
            if(!isVisited(currentState)) {        
                possibilities.push(new State(currentState.getName(), currentState.getWord(), currentState.isInicial(), currentState.isFinal()));
            }
            if(l!=null) {
                undoTransition(t, currentState, l.getSymbol());
            }
                
            else {
                undoTransition(t, currentState);
            }
        }
        
        if(l!=null && l.getSymbol()!='#') {
            letterTransitions = getTransitions(currentState, l , T);
        }
        for(Transition t : letterTransitions) {
            if(l!=null) {
                doTransition(t, currentState, l.getSymbol());
            }
            else {
                doTransition(t, currentState);
            }
            if(!isVisited(currentState)) {  
            
                possibilities.push(new State(currentState.getName(), currentState.getWord(), currentState.isInicial(), currentState.isFinal()));
            }
            if(l!=null) {
                undoTransition(t, currentState);
            }
            
        }
                
        System.out.println("Possibilities:");
        for(State s1 : possibilities) {
            System.out.println(s1.getName()+" "+s1.getWord());
        }
        
        System.out.println("");
        
        if(currentState.getWord().isEmpty() && currentState.isFinal()) {
               hasPath = true;
               return;
        }
        
        while(!possibilities.empty() && !hasPath) {
            currentState = possibilities.pop();
                visited.add(currentState);
                
                findPath(currentState);
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
        
        
        /*
        Debug mode
        String word = "0000111";
        Letter l = new Letter('0');
        State s = S.get(0);
        s.setWord(word);
        ArrayList<State> possibilities = new ArrayList<State>();
        ArrayList<Transition> lambdaTransitions = getLambdaTransitions(s, T);
        for(Transition t : lambdaTransitions) {
            doTransition(t, s);
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        ArrayList<Transition> letterTransitions = getTransitions(s, l, T);
        for(Transition t : letterTransitions) {
            doTransition(t, s);
            System.out.println("word after transitions: "+s.getWord());
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        
        System.out.println("Possibilities");
        for (State s1 : possibilities) {
            System.out.println(s1.getName()+" "+s1.getWord());
        }
        */
        
        //State s = S.get(3);
        //System.out.println(s.getName()+" "+s.getFinal());
        
        //System.out.println(isFinal("i1"));
        /*
        Debug mode
        String word = "0000111";
        Letter l = new Letter('0');
        State s = S.get(0);
        s.setWord(word);
        ArrayList<State> possibilities = new ArrayList<State>();
        ArrayList<Transition> lambdaTransitions = getLambdaTransitions(s, T);
        for(Transition t : lambdaTransitions) {
            doTransition(t, s);
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        ArrayList<Transition> letterTransitions = getTransitions(s, l, T);
        for(Transition t : letterTransitions) {
            doTransition(t, s);
            System.out.println("word after transitions: "+s.getWord());
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        
        System.out.println("Possibilities");
        for (State s1 : possibilities) {
            System.out.println(s1.getName()+" "+s1.getWord());
        }
        */
        
        //State s = S.get(3);
        //System.out.println(s.getName()+" "+s.getFinal());
        
        //System.out.println(isFinal("i1"));
        
        Transition stationary = new Transition(s, new Letter('#'), s);
        lambdaTransitions.add(stationary);
        
        for(Transition t : T) {
            if(t.from().getName().equals(s.getName()) && t.letter().getSymbol()=='#') {
                lambdaTransitions.add(t);
        
        
        /*
        Debug mode
        String word = "0000111";
        Letter l = new Letter('0');
        State s = S.get(0);
        s.setWord(word);
        ArrayList<State> possibilities = new ArrayList<State>();
        ArrayList<Transition> lambdaTransitions = getLambdaTransitions(s, T);
        for(Transition t : lambdaTransitions) {
            doTransition(t, s);
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        ArrayList<Transition> letterTransitions = getTransitions(s, l, T);
        for(Transition t : letterTransitions) {
            doTransition(t, s);
            System.out.println("word after transitions: "+s.getWord());
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        
        System.out.println("Possibilities");
        for (State s1 : possibilities) {
            System.out.println(s1.getName()+" "+s1.getWord());
        }
        */
        
        //State s = S.get(3);
        //System.out.println(s.getName()+" "+s.getFinal());
        
        //System.out.println(isFinal("i1"));
        /*
        Debug mode
        String word = "0000111";
        Letter l = new Letter('0');
        State s = S.get(0);
        s.setWord(word);
        ArrayList<State> possibilities = new ArrayList<State>();
        ArrayList<Transition> lambdaTransitions = getLambdaTransitions(s, T);
        for(Transition t : lambdaTransitions) {
            doTransition(t, s);
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        ArrayList<Transition> letterTransitions = getTransitions(s, l, T);
        for(Transition t : letterTransitions) {
            doTransition(t, s);
            System.out.println("word after transitions: "+s.getWord());
            possibilities.add(new State(s.getName(), s.getWord(), s.isI(), s.isF()));
            undoTransition(t, s);
        }
        
        System.out.println("Possibilities");
        for (State s1 : possibilities) {
            System.out.println(s1.getName()+" "+s1.getWord());
        }
        */
        
        //State s = S.get(3);
        //System.out.println(s.getName()+" "+s.getFinal());
        
        //System.out.println(isFinal("i1"));
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
    
    public static void doTransition(Transition t, State s, char lastSymbolRead) {
        if(t.letter().getSymbol()!='#')
            s.setWord(forward(s.getWord()));
        else if(t.letter().getSymbol()=='#' && lastSymbolRead=='#')
            s.setWord(forward(s.getWord()));
        s.setName(t.to().getName());
        s.setInicial(t.to().isInicial());
        s.setFinal(t.to().isFinal());
    }
    
    public static void doTransition(Transition t, State s) {
        if(t.letter().getSymbol()!='#')
            s.setWord(forward(s.getWord()));
        s.setName(t.to().getName());
        s.setInicial(t.to().isInicial());
        s.setFinal(t.to().isFinal());
    }
    
    public static void undoTransition(Transition t, State s, char lastSymbolRead) {
        if(t.letter().getSymbol()!='#')
            s.setWord(backward(s.getWord(), t.letter()));
        else if(t.letter().getSymbol()=='#' && lastSymbolRead=='#')
            s.setWord(backward(s.getWord(), t.letter()));
        s.setName(t.from().getName());
        s.setInicial(t.from().isInicial());
        s.setFinal(t.from().isFinal());
    }
    
    public static void undoTransition(Transition t, State s) {
        if(t.letter().getSymbol()!='#')
            s.setWord(backward(s.getWord(), t.letter()));
        s.setName(t.from().getName());
        s.setInicial(t.from().isInicial());
        s.setFinal(t.from().isFinal());
    }
    
    public static String forward(String s) {
        s = s.substring(1, s.length());
        return s;
    }
    
    public static String backward(String s, Letter l) {
        return (l.getSymbol() + s);
    }
}
