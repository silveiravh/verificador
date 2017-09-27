package parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class LexicalAnalysis implements AutoCloseable {
    private int line;
    private PushbackInputStream input;
    private SymbolTable st = new SymbolTable();
    
    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }
        
        line = 1;
    }

    public void close() throws Exception {
        input.close();
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() throws IOException {
        Lexeme l = new Lexeme("", TokenType.END_OF_FILE);
        int e = 1, c;
        while(e!=9 && e!=10){
            c = input.read();
            switch(e){
                case 1:
                    if (c == -1){
                        e = 10;
                    }else if(c==' ' || c == '\t' || c == '\r'){
                        e= 1;
                    }else if(c == '\n'){
                        e =1;
                        line++;
                    }else if(c == '#'){
                        e = 2;
                    }else if(Character.isDigit(c)){
                        l.token += (char)c;
                        e = 3;
                    }else if(c == '=' || c == '!'){
                        l.token += (char)c;
                        e = 4;
                    }else if(c == '<' || c == '>'){
                        l.token += (char)c;
                        e = 5;
                    }else if(c == '-'){
                        l.token += (char)c;
                        e = 6;
                    }else if(Character.isAlphabetic(c)){
                        l.token += (char)c;
                        e = 7;
                    }else if( c== '\"'){
                        e = 8;
                    }else{
                        e = 9;
                        l.token += (char) c;
                        switch(c){
                            case ';':
                                l.type = TokenType.DOT_COMMA;
                                break;
                            case ':':
                                l.type = TokenType.SEMI_COLON;
                                break;
                            case '.':
                                l.type = TokenType.DOT;
                                break;
                            case ',':
                                l.type = TokenType.COMMA;
                                break;
                            case '(':
                                l.type = TokenType.PAR_OPEN;
                                break;
                            case ')':
                                l.type = TokenType.PAR_CLOSE;
                                break;
                            case '{':
                                l.type = TokenType.CBRA_OPEN;
                                break;
                            case '}':
                                l.type = TokenType.CBRA_CLOSE;
                                break;
                            case '[':
                                l.type = TokenType.SBRA_OPEN;
                                break;
                            case ']':
                                l.type = TokenType.SBRA_CLOSE;
                                break;
                            case '+':
                                l.type = TokenType.PLUS;
                                break;
                            case '*':
                                l.type = TokenType.MUL;
                                break;
                            case '/':
                                l.type = TokenType.DIV;
                                break;
                            case '%':
                                l.type = TokenType.MOD;
                                break;
                            default:
                                l.type = TokenType.INVALID_TOKEN;
                                e = 10;
                                break;
                        }
                        
                    }
                    break;
                case 2:
                    if(c == '\n'){
                        e = 1;
                        line++;
                    }
                    break;

                case 3:
                    if (Character.isDigit(c))
                        l.token += (char) c;
                    else if(c!=-1) {
                        input.unread(c);
                        e = 10;
                        l.type = TokenType.NUMBER;
                    }
                    break;
                case 4: 
                    if(c=='=') {
                        if(l.token == "="){
                            l.type = TokenType.EQUAL;
                        }else{
                            l.type = TokenType.DIFF;                            
                        }
                        l.token += (char) c;
                        e = 9;
                    }
                    else if(c==-1){
                        l.type = TokenType.UNEXPECTED_EOF;
                        e = 10; //conferir
                    }
                    else{
                        l.type = TokenType.INVALID_TOKEN;
                        e = 10;//conferir
                    }
                    //e=10 linha não fazia sentido
                    break;
                case 5:
                    if(c == '='){
                        l.token += (char) c;
                        e = 9;
                        switch(l.token){
                            case "<":
                                l.type = TokenType.LOWER_EQ;
                                break;
                            case ">":
                                l.type = TokenType.HIGHER_EQ;
                                break;
                        }
                    }else{
                        input.unread(c);
                        switch(l.token){
                            case "<":
                                e = 9;
                                l.type = TokenType.LOWER;
                                break;
                            case ">":
                                e = 9;
                                l.type = TokenType.HIGHER;
                                break;
                        }
                    }
                    break;
                case 6:
                    
                    if(c == '>'){
                        l.type = TokenType.ARROW;
                        l.token += (char) c;
                    }else{
                        l.type = TokenType.MINUS;
                        input.unread(c);
                    }
                    e = 9;
                    break;
                case 7:
                    if(Character.isDigit(c) || Character.isAlphabetic(c)){
                       l.token += (char) c;
                    }else{
                        input.unread(c);
                        e=9;
                        switch(l.token){
                            case "print":
                                l.type = TokenType.PRINT;
                                break;
                            case "println":
                                l.type = TokenType.PRINTLN;
                                break;
                            case "if":
                                l.type = TokenType.IF;
                                break;
                            case "else":
                                l.type = TokenType.ELSE;
                                break;
                            case "while":
                                l.type = TokenType.WHILE;
                                break;
                            case "load":
                                l.type = TokenType.LOAD;
                                break;
                            case "new":
                                l.type = TokenType.NEW;
                                break;
                            case "zero":
                                l.type = TokenType.ZERO;
                                break;
                            case "rand":
                                l.type = TokenType.RAND;
                                break;
                            case "fill":
                                l.type = TokenType.FILL;
                                break;
                            case "show":
                                l.type = TokenType.SHOW;
                                break;
                            case "sort":
                                l.type = TokenType.SORT;
                                break;
                            case "add":
                                l.type = TokenType.ADD;
                                break;
                            case "set":
                                l.type = TokenType.SET;
                                break;
                            case "filter":
                                l.type = TokenType.FILTER;
                                break;
                            case "remove":
                                l.type = TokenType.REMOVE;
                                break;
                            case "each":
                                l.type = TokenType.EACH;
                                break;
                            case "apply":
                                l.type = TokenType.APPLY;
                                break;
                            case "at":
                                l.type = TokenType.AT;
                                break;
                            case "size":
                                l.type = TokenType.SIZE;
                                break;
                            case "and":
                                l.type = TokenType.AND;
                                break;
                            case "or":
                                l.type = TokenType.OR;
                                break;
                        }
                    }
                    break;
                case 8: 
                    if(c=='\"') {
                        e = 10;
                        l.type = TokenType.STRING;
                    }
                    else if(c==-1){
                        l.type = TokenType.UNEXPECTED_EOF;
                        e = 10;
                    }
                    else{
                        l.token += (char) c;
                    }
                    break;
                    
            }	
            
        }
        if(e == 9){
            if(st.contains(l.token)){
                l.type = st.find(l.token);
            }else{
                l.type = TokenType.VAR;
            }                
        }
         // TODO: implement me.

         // HINT: read a char.
         // int c = input.read();

         // HINT: unread a char.
         // if (c != -1)
         //     input.unread(c);
        if(l.type == TokenType.UNEXPECTED_EOF){
            System.out.println(getLine()+": Fim de arquivo inesperado");
            System.exit(1);
        }else if(l.type == TokenType.INVALID_TOKEN){
            System.out.println(getLine()+": Lexema inválido ["+l.token+"]");
            System.exit(1);
        }
         
         return l;
    }
}
