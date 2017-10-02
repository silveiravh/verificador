/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntatical;

import semantical.Letter;
import semantical.Transition;
import semantical.State;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static semantical.State.setFinal;
import static semantical.State.setInicial;
import static syntatical.LexicalAnalysis.line;
import static syntatical.Verificador.S;
import static syntatical.Verificador.L;
import static syntatical.Verificador.T;
import static syntatical.Verificador.I;
import static syntatical.Verificador.F;

/**
 *
 * @author silveira
 */
public class SyntaticalAnalysis {
    private Lexeme current;
    private LexicalAnalysis lex;
    
    public SyntaticalAnalysis(LexicalAnalysis lex) throws IOException{
        this.lex = lex;
        this.current = lex.nextToken();
    }
    
    private void matchToken(TokenType type) throws IOException{
        try {
            if(type == current.type){
                current = lex.nextToken();
            }
            else {
                throw new IOException(line+": Lexama não esperado ["+current.token+"]"+current.type+"esperado: "+type);
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    // <af> ::= '{' "af" ':' '[' <states> ',' <letters> ',' <transitions> ',' <inicials> ',' <finals> ']' '}' 
    public void procAf() throws IOException {
        matchToken(TokenType.CBRA_OPEN);
        System.out.println("af: "+current.token);
        matchToken(TokenType.AF);
        matchToken(TokenType.COLON);
        matchToken(TokenType.SBRA_OPEN);
        System.out.println("state: "+current.token);
        S = procStates();
        matchToken(TokenType.COMMA);
        System.out.println("Before letters:"+current.token);
        L = procLetters();
        System.out.println("Before transition: "+current.token);
        matchToken(TokenType.COMMA);
        T = procTransitions();
        matchToken(TokenType.COMMA);
        I = procInicials();
        matchToken(TokenType.COMMA);
        F = procFinals();
        matchToken(TokenType.SBRA_CLOSE);
        matchToken(TokenType.CBRA_CLOSE);
    }
    
    //<states> ::= '[' <state> {, <state> } ']'
    private ArrayList<State> procStates() throws IOException {
        try {
            matchToken(TokenType.SBRA_OPEN);
            State s = procState();
            S.add(s);
            while(this.current.type==TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                s = procState();
                S.add(s);
            }
            if(this.current.type==TokenType.SBRA_CLOSE) {
                matchToken(TokenType.SBRA_CLOSE);
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
        return S;
    }
    
    private State procState() throws IOException {
        try {
            if(this.current.type==TokenType.STATE) {
                State s = new State(current.token);
                matchToken(TokenType.STATE);
                return s;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //<letters> ::= '[' <letter> {, <letter> } ']'
    private ArrayList<Letter> procLetters() throws IOException {
        try {
            matchToken(TokenType.SBRA_OPEN);
            Letter l = procLetter();
            L.add(l);
            while(this.current.type==TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                l = procLetter();
                L.add(l);
            }
            if(this.current.type==TokenType.SBRA_CLOSE) {
                matchToken(TokenType.SBRA_CLOSE);
                return L;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
   
    private Letter procLetter() throws IOException {
        try {
            if(this.current.type==TokenType.LETTER) {
                Letter l = new Letter(current.token.charAt(0));
                matchToken(TokenType.LETTER);
                return l;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //<transitions> ::= '[' <state> ',' <letter> ',' <state> ']' {, '[' <state> ',' <letter> ',' <state> ']'}
    private ArrayList<Transition> procTransitions() throws IOException {
        try {
            System.out.println("Current: "+current.token);
            matchToken(TokenType.SBRA_OPEN);
            matchToken(TokenType.SBRA_OPEN);
            State from = procState();
            System.out.println(from);
            matchToken(TokenType.COMMA);
            Letter letter = procLetter();
            matchToken(TokenType.COMMA);
            State to = procState();
            Transition t = new Transition(from, letter, to);
            T.add(t);
            matchToken(TokenType.SBRA_CLOSE);
            while(this.current.type==TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                matchToken(TokenType.SBRA_OPEN);
                from = procState();
                matchToken(TokenType.COMMA);
                letter = procLetter();
                matchToken(TokenType.COMMA);
                to = procState();
                t = new Transition(from, letter, to);
                T.add(t);
                matchToken(TokenType.SBRA_CLOSE);
            }
            if(this.current.type==TokenType.SBRA_CLOSE) {
                matchToken(TokenType.SBRA_CLOSE);
                return T;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //<inicials> ::= '['<state> {, <state> '} ']'
    private ArrayList<State> procInicials() throws IOException {
        try {
            matchToken(TokenType.SBRA_OPEN);
            State s = procState();
            setInicial(s);
            while(this.current.type==TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                s = procState();
                setInicial(s);
            }
            if(this.current.type==TokenType.SBRA_CLOSE) {
                matchToken(TokenType.SBRA_CLOSE);
                return S;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //<finals> ::= '[' <state> {, <state> } ']'
    private ArrayList<State> procFinals() throws IOException  {
        try {
            matchToken(TokenType.SBRA_OPEN);
            State s = procState();
            setFinal(s);
            while(this.current.type==TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                s = procState();
                setFinal(s);
            }
            if(this.current.type==TokenType.SBRA_CLOSE) {
                matchToken(TokenType.SBRA_CLOSE);
                return S;
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
