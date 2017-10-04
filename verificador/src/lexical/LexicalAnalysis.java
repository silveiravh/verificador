/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical;

import lexical.SymbolTable;
import lexical.TokenType;
import lexical.Lexeme;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 *
 * @author silveira
 */
public class LexicalAnalysis {
    public static int line;
    private PushbackInputStream input;
    private SymbolTable st = new SymbolTable();
    
    public LexicalAnalysis(String filename) throws IOException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new IOException("Unable to open file");
        }
        
        line = 1;
    }

    public Lexeme nextToken() throws IOException {
        Lexeme l = new Lexeme("", TokenType.END_OF_FILE);
        int e = 1, c=0;
        while(e!=3 && e!=4){
            c = input.read();
            switch(e){
                case 1: if(c==-1) {
                            l.type = TokenType.END_OF_FILE;
                            e = 4;
                        }
                        else if(c==' ' || c=='\t' || c=='\r') {
                            e = 1;
                        }
                        else if(c=='\n') {
                            line++;
                            e = 1;
                        }
                        else if(c=='\"') {
                            e = 2;
                        }
                        else {
                            switch(c) {
                                case '{': l.type = TokenType.CBRA_OPEN;
                                          e = 3;
                                          break;
                                case '}': l.type = TokenType.CBRA_CLOSE;
                                          e = 3;
                                          break;
                                case ':': l.type = TokenType.COLON;
                                          e = 3;
                                          break;
                                case '[': l.type = TokenType.SBRA_OPEN;
                                          e = 3;
                                          break;
                                case ']': l.type = TokenType.SBRA_CLOSE;
                                          e = 3;
                                          break;
                                case ',': l.type = TokenType.COMMA;
                                          e = 3;
                                          break;
                                default: l.type = TokenType.INVALID_TOKEN;
                                         e = 4;
                            }
                        }
                        break;
                
                case 2: if(c==-1) {
                            l.type = TokenType.UNEXPECTED_EOF;
                            e = 4;
                        }
                        else if(Character.isAlphabetic(c) || Character.isDigit(c) || c=='#') {
                            l.token += (char)c;
                            e = 2;
                        }
                        else if(c=='\"') {
                            e = 4;
                        } 
            }
        }
        if(e==4) {
            if(l.token.length()==1) {
                l.type = TokenType.LETTER;
            }
            else if(l.token.equals("af")) {
              l.type = TokenType.AF;
            }
            else if(l.type==TokenType.UNEXPECTED_EOF) {
                throw new IOException(line+": Fim de arquivo inesperado");
            }
            else if(l.type==TokenType.INVALID_TOKEN) {
                throw new IOException(line+": Lexama invalido ["+(char)c+"]");
            }
            else{
                l.type = TokenType.STATE;
            }
        }
        return l;
    }
}