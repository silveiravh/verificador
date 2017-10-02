/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificador;

import java.io.IOException;
import java.util.ArrayList;
import semantical.Letter;
import static semantical.Letter.printLetters;
import semantical.State;
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
    }
}
