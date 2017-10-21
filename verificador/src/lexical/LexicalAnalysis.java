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
            System.out.println("Unable to open file "+filename);
            System.exit(1);
        }
        
        line = 1;
    }

    public Lexeme nextToken() throws IOException {
        Lexeme l = new Lexeme("", null);
        int e = 1, c = 0;
        String token = "";
        while(e!=3 && e!=4){
            c = input.read();
            token += (char)c;
            switch(e){
                case 1: if(c==-1) {
                            l.type = TokenType.END_OF_FILE;
                            e = 4;
                            break;
                        }
                        else if(c==' ' || c=='\t' || c=='\r') {
                            e = 1;
                        }
                        else if(c=='\n') {
                            line++;
                            e = 1;
                            break;
                        }
                        else if(c=='\"') {
                            e = 2;
                            break;
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
                            break;
                        }
                        else if(c==' ' || c=='\t' || c=='\r') {
                            e = 2;
                        }
                        else if(c=='\n') {
                            line++;
                            e = 2;
                            break;
                        }
                        else if(Character.isLetterOrDigit(c) || c=='#') {
                            l.token += (char)c;
                            e = 2;
                            break;
                        }
                        else if(c=='\"') {
                            e = 4;
                            break;
                        }
                        token = ""+(char)c;
                        if(!st.contains(token)){
                            l.type = TokenType.INVALID_TOKEN;
                            e = 4;
                            break;
                        }
                        else {
                            l.type = TokenType.UNEXPECTED_TOKEN;
                            e = 4;
                        }
            }
        }
        if(e==4) {
            if(l.type==TokenType.UNEXPECTED_TOKEN) {
                throw new IOException(line+": Unexpected lexeme ["+token+"]");
            }
            else if(l.type==TokenType.UNEXPECTED_EOF) {
                throw new IOException(line+": Unexpected end of file");
            }
            else if(l.type==TokenType.INVALID_TOKEN) {
                throw new IOException(line+": Invalid lexeme ["+(char)c+"]");
            }
            else if(l.type!=TokenType.END_OF_FILE){
                l.type = TokenType.STRING;
            }
        }
        return l;
    }
}