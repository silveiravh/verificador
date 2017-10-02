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
    
    private static Stack<Transition> possibilities = new Stack<Transition>();
    private static Stack<Transition> path = new Stack<Transition>();
    private static Stack<Transition> visited = new Stack<Transition>();
    private static Stack<Integer> popOrder = new Stack<Integer>();
    
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
            SyntaticalAnalysis s = new SyntaticalAnalysis((l));
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
            int possibilitiesNumber = 0;
            ArrayList<Transition> lambdaTransitions = getLambda(i);
            //possibilitiesNumber += lambdaTransitions.size();
            for(Transition t1 : lambdaTransitions) {
                if(!isVisited(t1)) {
                    possibilities.push(t1);
                    possibilitiesNumber++;
                }
            }
            ArrayList<Transition> transitions = getTransitions(i, getNextLetter(word));
            //possibilitiesNumber += transitions.size();
            for(Transition t2 : transitions) {
                if(isVisited(t2)) {
                    possibilities.push(t2);
                    possibilitiesNumber++;
                }
            }
            if(possibilitiesNumber==0) {
                System.out.println("Não");
                return;
            }
            if(possibilitiesNumber>=2) {
                popOrder.push(possibilities.size());
            }
            Transition t = possibilities.pop();
            path.push(t);
            currentState = doTransition(t);
            if(word.isEmpty() && currentState.isFinal()) {
                System.out.println("Sim");
                return;
            }
            else if(word.isEmpty() && !currentState.isFinal() && possibilities.size()==0) {
                System.out.println("Não");
            }
            else if(word.isEmpty() && !currentState.isFinal() && possibilities.size()!=0) {
                int backTransitionsNumber = popOrder.pop();
                while(path.size()!=backTransitionsNumber) {
                    Transition backTransition = path.pop();
                    currentState = undoTransition(backTransition);
                    if(path.size()==backTransitionsNumber-1) {
                        visited.add(t);
                    }
                }
            }
            
            while(!word.isEmpty()) {
                possibilitiesNumber = 0;
                lambdaTransitions = getLambda(i);
                //possibilitiesNumber += lambdaTransitions.size();
                for(Transition t3 : lambdaTransitions) {
                    if(!isVisited(t3)) {
                        possibilities.push(t3);
                        possibilitiesNumber++;
                    }
                }
                transitions = getTransitions(i, getNextLetter(word));
                //possibilitiesNumber += transitions.size();
                for(Transition t4 : transitions) {
                    if(!isVisited(t4)) {
                        possibilities.push(t4);
                        possibilitiesNumber++;
                    }
                }
                if(possibilitiesNumber==0) {
                    System.out.println("Não");
                    return;
                }
                if(possibilitiesNumber>=2) {
                    popOrder.push(possibilities.size());
                }
                t = possibilities.pop();
                path.push(t);
                currentState = doTransition(t);
                if(word.isEmpty() && currentState.isFinal()) {
                    System.out.println("Sim");
                    return;
                }
                else if(word.isEmpty() && !currentState.isFinal() && possibilities.size()==0) {
                    System.out.println("Não");
                }
                else if(word.isEmpty() && !currentState.isFinal() && possibilities.size()!=0) {
                    int backTransitionsNumber = popOrder.pop();
                    while(path.size()!=backTransitionsNumber) {
                        Transition backTransition = path.pop();
                        currentState = undoTransition(backTransition);
                        if(path.size()==backTransitionsNumber-1) {
                        visited.add(t);
                        }
                    }
                }
            }
        }
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
    
    public static ArrayList<Transition> getLambda(State s) {
        ArrayList<Transition> lambdaTransitions = new ArrayList<Transition>();
        
        for(Transition t : T) {
            if(t.from().getName().equals(s.getName()) && t.letter().getSymbol()=='#') {
                lambdaTransitions.add(t);
            }
        }
        
        return lambdaTransitions;
    }
    
    public static Letter getNextLetter(String word) {
        Letter l = new Letter(word.charAt(0));
        
        return l;
    }
    
    public static ArrayList<Transition> getTransitions(State s, Letter l) {
        ArrayList<Transition> transitions = new ArrayList<Transition>();
        
        for(Transition t : T) {
            if(t.from().getName().equals(s.getName()) && t.letter().getSymbol()==l.getSymbol()) {
                transitions.add(t);
            }
        }
        
        return transitions;
    }
    
    public static State doTransition(Transition t) {
        forward(word);
        return t.to();
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
