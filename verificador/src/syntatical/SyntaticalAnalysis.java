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
    
    private void showError(){
        if(current.type == TokenType.END_OF_FILE){
            errorUnexpectedEOF();
        }else{
            errorUnexpectedToken(current.token);
        }
    }
    
    private void errorUnexpectedToken(String token){
        System.out.println(lex.line+": Lexema não esperado ["+token+"]");
        System.exit(1);
    }
    
    private void errorUnexpectedEOF(){
        System.out.println(lex.line+": Fim de arquivo inesperado");
        System.exit(1);
    }
    
    private void matchToken(TokenType type) throws IOException{
        if(type == current.type){
            current = lex.nextToken();
        }else{
            showError();
        }
    }
    
    // <af> ::= '{' '"' af '"' ':' '[' <states> ',' <letters> ',' <transitions> ',' <inicials> ',' <finals> ']' '}' 
    private void procAf() throws IOException {
        matchToken(TokenType.CBRA_OPEN);
        matchToken(TokenType.QUOTATION_MARK);
        matchToken(TokenType.AF);
        matchToken(TokenType.QUOTATION_MARK);
        matchToken(TokenType.COLON);
        matchToken(TokenType.SBRA_OPEN);
        S = procStates();
        L = procLetters();
        T = procTransitions();
        I = procInicials();
        F = procFinals();
        matchToken(TokenType.SBRA_CLOSE);
        matchToken(TokenType.CBRA_CLOSE);
    }
    
    //<states> ::= '[' '"' <state> '"' {, '"' <state> '"'} ']'
    private ArrayList<State> procStates() throws IOException {
        matchToken(TokenType.SBRA_OPEN);
        matchToken(TokenType.QUOTATION_MARK);
        State s = procState();
        S.add(s);
        matchToken(TokenType.QUOTATION_MARK);
        while(this.current.type==TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            s = procState();
            S.add(s);
            matchToken(TokenType.QUOTATION_MARK);
        }
        if(this.current.type==TokenType.SBRA_CLOSE) {
            matchToken(TokenType.SBRA_CLOSE);
        }
        else {
            showError();
        }
        return S;
    }
    
    private State procState() throws IOException {
        if(this.current.type==TokenType.STATE) {
            State s = new State(current.token);
            matchToken(TokenType.STATE);
            return s;
        }
        else {
            showError();
            return null;
        }
    }
    
    //<letters> ::= '[' '"' <letter> '"' {, '"' <letter> '"'} ']'
    private ArrayList<Letter> procLetters() throws IOException {
        matchToken(TokenType.SBRA_OPEN);
        matchToken(TokenType.QUOTATION_MARK);
        Letter l = procLetter();
        L.add(l);
        matchToken(TokenType.QUOTATION_MARK);
        while(this.current.type==TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            l = procLetter();
            L.add(l);
            matchToken(TokenType.QUOTATION_MARK);
        }
        if(this.current.type==TokenType.SBRA_CLOSE) {
            matchToken(TokenType.SBRA_CLOSE);
            return L;
        }
        else {
            showError();
            return null;
        }
    }
   
    private Letter procLetter() throws IOException {
        if(this.current.type==TokenType.LETTER) {
            Letter l = new Letter(current.token.charAt(0));
            matchToken(TokenType.LETTER);
            return l;
        }
        else {
            showError();
            return null;
        }
    }
    
    //<transitions> ::= '[' '"' <state> '"' ',' '"' <letter> '"' ',' '"' <state> '"' ']' {, '[' '"' <state> '"' ',' '"' <letter> '"' ',' '"' <state> '"' ']'}
    private ArrayList<Transition> procTransitions() throws IOException {
        matchToken(TokenType.SBRA_CLOSE);
        matchToken(TokenType.QUOTATION_MARK);
        State from = procState();
        matchToken(TokenType.QUOTATION_MARK);
        matchToken(TokenType.COMMA);
        matchToken(TokenType.QUOTATION_MARK);
        Letter letter = procLetter();
        matchToken(TokenType.QUOTATION_MARK);
        matchToken(TokenType.COMMA);
        matchToken(TokenType.QUOTATION_MARK);
        State to = procState();
        matchToken(TokenType.QUOTATION_MARK);
        Transition t = new Transition(from, letter, to);
        T.add(t);
        while(this.current.type==TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            from = procState();
            matchToken(TokenType.QUOTATION_MARK);
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            letter = procLetter();
            matchToken(TokenType.QUOTATION_MARK);
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            to = procState();
            matchToken(TokenType.QUOTATION_MARK);
            t = new Transition(from, letter, to);
            T.add(t);
        }
        if(this.current.type==TokenType.SBRA_CLOSE) {
            matchToken(TokenType.SBRA_CLOSE);
            return T;
        }
        else {
            showError();
            return null;
        }
    }
    
    //<inicials> ::= '[' '"' <state> '"' {, '"' <state> '"'} ']'
    private ArrayList<State> procInicials() throws IOException {
        matchToken(TokenType.SBRA_OPEN);
        matchToken(TokenType.QUOTATION_MARK);
        State s = procState();
        setInicial(s);
        matchToken(TokenType.QUOTATION_MARK);
        while(this.current.type==TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            s = procState();
            setInicial(s);
            matchToken(TokenType.QUOTATION_MARK);
        }
        if(this.current.type==TokenType.SBRA_CLOSE) {
            matchToken(TokenType.SBRA_CLOSE);
            return S;
        }
        else {
            showError();
            return null;
        }
    }
    
    //<finals> ::= '[' '"' <state> '"' {, '"' <state> '"'} ']'
    private ArrayList<State> procFinals() throws IOException  {
        matchToken(TokenType.SBRA_OPEN);
        matchToken(TokenType.QUOTATION_MARK);
        State s = procState();
        setFinal(s);
        matchToken(TokenType.QUOTATION_MARK);
        while(this.current.type==TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            matchToken(TokenType.QUOTATION_MARK);
            s = procState();
            setFinal(s);
            matchToken(TokenType.QUOTATION_MARK);
        }
        if(this.current.type==TokenType.SBRA_CLOSE) {
            matchToken(TokenType.SBRA_CLOSE);
            return S;
        }
        else {
            showError();
            return null;
        }
    }
}
