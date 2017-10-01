/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntatical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 *
 * @author silveira
 */
public class LexicalAnalysis {
    protected int line;
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

    public void close() throws IOException {
        input.close();
    }

    public Lexeme nextToken() throws IOException {
        Lexeme l = new Lexeme("", TokenType.END_OF_FILE);
        int e = 1, c;
        while(e!=3 && e!=4){
            c = input.read();
            switch(e){
                case 1: if(c==-1) {
                            l.type = TokenType.UNEXPECTED_EOF;
                            e = 4;
                        }  
                        else if(c==' ' || c=='\t') {
                            e = 1;
                        }
                        else if(c=='\n') {
                            line++;
                            e = 1;
                        }
                        else if(c=='{' || c=='}' || c==':' || c=='[' || c==']' || c==',') {
                            e = 3;
                        }
                        else if(c=='\"') {
                            e = 2;
                        }
                        else {
                            l.type = TokenType.INVALID_TOKEN;
                        }
                        break;
                
                case 2: if(c==-1) {
                            l.type = TokenType.UNEXPECTED_EOF;
                            e = 4;
                        }
                        if(Character.isAlphabetic(c) || Character.isDigit(c)) {
                            l.token += (char)c;
                            e = 2;
                        }
                        if(c=='\"') {
                            e = 4;
                        }
                        else {
                            l.type = TokenType.INVALID_TOKEN;
                        }
                          
                        if(e==4) {
                            if(l.token.length()==1) {
                                l.type = TokenType.LETTER;
                            }
                            else if(l.token.equals("af")) {
                              l.type = TokenType.AF;
                            }
                            else if(l.type==TokenType.UNEXPECTED_EOF) {
                                System.out.println(line+": Fim de arquivo inesperado");
                            }
                            else if(l.type==TokenType.INVALID_TOKEN) {
                                System.out.println(line+": Lexama invalido ["+(char)c+"]");
                            }
                        }
            }
        }
        return l;
    }
}