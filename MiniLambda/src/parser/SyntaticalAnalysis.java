/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import Semantical.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lucasmarioza
 */
public class SyntaticalAnalysis {
    private Lexeme current;
    private LexicalAnalysis lex;
    private Map<String,Variable> vars = new HashMap<String,Variable>();
    
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
        System.out.println(lex.getLine()+": Lexema não esperado ["+token+"]");
        System.exit(1);
    }
    
    private void errorUnexpectedEOF(){
        System.out.println(lex.getLine()+": Fim de arquivo inesperado");
        System.exit(1);
    }
    
    private void matchToken(TokenType type) throws IOException{
        if(type == current.type){
            current = lex.nextToken();
        }else{
            showError();
        }
    }
    
    //<load> ::= load '(' <text> ')' 
    
    private LoadIntValue procLoad() throws IOException{
        matchToken(TokenType.LOAD);
        matchToken(TokenType.PAR_OPEN);
        Value<?> text = procText();
        matchToken(TokenType.PAR_CLOSE);
        LoadIntValue li = new LoadIntValue(text, lex.getLine());
        return li;
    }
    
    //<assign> ::=  <expr> [ ':' <var> {‘,’ <var> } ] ';'
    private AssignCommand procAssign() throws IOException{
        AssignCommand ac = new AssignCommand(procExpr(), lex.getLine());
        if(current.type == TokenType.SEMI_COLON){
            matchToken(TokenType.SEMI_COLON);
            ac.addVariable(procVar());
            while(current.type == TokenType.COMMA){
                matchToken(TokenType.COMMA);
                ac.addVariable(procVar());
            }
        }
        matchToken(TokenType.DOT_COMMA);
        return ac;
    }
    
    //<print> ::= (print | println) '(' <text> ')';
    private PrintCommand procPrint() throws IOException{
        int line = lex.getLine();
        boolean newLine;
        if(current.type == TokenType.PRINT){
            newLine =false;
            matchToken(TokenType.PRINT);
        }else if(current.type == TokenType.PRINTLN){
            newLine=true;
            matchToken(TokenType.PRINTLN);
        }else{
            newLine = false;
            showError();
        }
        matchToken(TokenType.PAR_OPEN);
        Value<?> v = procText();
        matchToken(TokenType.PAR_CLOSE);
        matchToken(TokenType.DOT_COMMA);
        
        PrintCommand pc = new PrintCommand(v,newLine,line);
        return pc;
    }

    //<if> ::= if <boolexpr> '{' <statements> '}' [ else '{' <statements> '}']
    private IfCommand procIf() throws IOException{
        matchToken(TokenType.IF);
        BoolValue be = procBoolExpr();
        matchToken(TokenType.CBRA_OPEN);
        Command thenCmd = procStatements();
        matchToken(TokenType.CBRA_CLOSE);
        IfCommand ic;
        if(current.type == TokenType.ELSE){
            matchToken(TokenType.ELSE);
            matchToken(TokenType.CBRA_OPEN);
            Command elseCmd = procStatements();
            matchToken(TokenType.CBRA_CLOSE);
            ic = new IfCommand(be,thenCmd,elseCmd,lex.getLine());
        }else{
            ic = new IfCommand(be,thenCmd,lex.getLine());
        }
        return ic;
    }
       
    //<while> ::= while <boolexpr> '{' <statements> '}'
    private WhileCommand procWhile() throws IOException{
        matchToken(TokenType.WHILE);
        BoolValue bv = procBoolExpr();
        matchToken(TokenType.CBRA_OPEN);
        Command cmd = procStatements();
        matchToken(TokenType.CBRA_CLOSE);
        return new WhileCommand(bv,cmd,lex.getLine());
    }
    
    //<text> ::= (<string> | <expr>) { ‘,’ (<string> | <expr>) }
    private Value<?> procText() throws IOException{
        Value<?> v;
        if(current.type == TokenType.STRING){
            v = procString();
        }else if(current.type == TokenType.PLUS ||
                current.type == TokenType.MINUS ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.LOAD ||
                current.type == TokenType.NEW ||
                current.type == TokenType.VAR ||
                current.type == TokenType.PAR_OPEN){
            v = procExpr();
        }else{
            showError();
            return null;
        }
        
        while(current.type == TokenType.COMMA){
            matchToken(TokenType.COMMA);
            if(current.type == TokenType.STRING){
                v = new StringConcat(v,procString(),lex.getLine());
            }else if(current.type == TokenType.PLUS ||
                    current.type == TokenType.MINUS ||
                    current.type == TokenType.NUMBER ||
                    current.type == TokenType.LOAD ||
                    current.type == TokenType.NEW ||
                    current.type == TokenType.VAR ||
                    current.type == TokenType.PAR_OPEN){
                v = new StringConcat(v,procExpr(),lex.getLine());
            }else{
                showError();
            }
        }
        return v;
    }
    
    //<boolexpr> ::= <expr> <boolop> <expr> { (and | or) <boolexpr> }
    private BoolValue procBoolExpr() throws IOException{
        Value<?> left = procExpr();
        RelOp op = procBoolOp();
        Value<?> right = procExpr();
        BoolValue bv;
        bv = new CompareBoolValue(op, left, right, lex.getLine());
        while(current.type == TokenType.AND || current.type == TokenType.OR){
            if(current.type == TokenType.AND){
                matchToken(TokenType.AND);
                bv = new DualBoolExpr(BoolOp.And,bv,procBoolExpr(),lex.getLine());
            }else {
                matchToken(TokenType.OR);
                bv = new DualBoolExpr(BoolOp.Or,bv,procBoolExpr(),lex.getLine());
            }
        }
        return bv;
    }
    
    //<boolop> ::= '==' | '!=' | '<' | '>' | '<=' | '>='
    private RelOp procBoolOp() throws IOException{
        if(current.type == TokenType.EQUAL){
            matchToken(TokenType.EQUAL);
            return RelOp.Equal;
        }else if(current.type == TokenType.DIFF){
            matchToken(TokenType.DIFF);
            return RelOp.NotEqual;
        }else if(current.type == TokenType.LOWER){
            matchToken(TokenType.LOWER);
            return RelOp.LowerThan;
        }else if(current.type == TokenType.HIGHER){
            matchToken(TokenType.HIGHER);
            return RelOp.GreaterThan;
        }else if(current.type == TokenType.LOWER_EQ){
            matchToken(TokenType.LOWER_EQ);
            return RelOp.LowerEqual;
        }else if(current.type == TokenType.HIGHER_EQ){
            matchToken(TokenType.HIGHER_EQ);
            return RelOp.GreaterEqual;
        }else {
            showError();
            return null;
        }
    }
    
    //<expr> ::= <term> [ ('+' | '-') <term> ]
    private Value<?> procExpr() throws IOException{
        Value v = procTerm();
        if(current.type == TokenType.PLUS){
            matchToken(TokenType.PLUS);
            v = new DualIntExpr(IntOp.Add,v,procTerm(),lex.getLine());
        }else if(current.type == TokenType.MINUS){
            matchToken(TokenType.MINUS);
            v = new DualIntExpr(IntOp.Sub,v,procTerm(),lex.getLine());
        }
        return v;
    }
    
    //<term> ::= <factor> [ ('*' | '/' | '%') <factor> ]
    private Value<?> procTerm() throws IOException{
        Value<?> v = procFactor();
        if(current.type == TokenType.MUL){
            matchToken(TokenType.MUL);
            v = new DualIntExpr(IntOp.Mul,v,procFactor(),lex.getLine());
        }else if(current.type == TokenType.DIV){
            matchToken(TokenType.DIV);
            v = new DualIntExpr(IntOp.Div,v,procFactor(),lex.getLine());
        }else if(current.type == TokenType.MOD){
            matchToken(TokenType.MOD);
            v = new DualIntExpr(IntOp.Mod,v,procFactor(),lex.getLine());
        }
        return v;
    }
    
    //<factor> ::= [‘+’ | ‘-‘] <number> | <load> | <value> | '(' <expr> ')'
    private Value<?> procFactor() throws IOException{
        Value<?> v=null;
        if(current.type == TokenType.PLUS || current.type == TokenType.MINUS){
            matchToken(current.type);
            v=procNumber();
        }else if(current.type == TokenType.NUMBER){
            v=procNumber();
        }else if(current.type == TokenType.LOAD){
            v=procLoad();
        }else if(current.type == TokenType.NEW || current.type == TokenType.VAR){
            v=procValue();
        }else if(current.type == TokenType.PAR_OPEN ){
            matchToken(TokenType.PAR_OPEN);
            v=procExpr();
            matchToken(TokenType.PAR_CLOSE);
        }else{
            showError();
        }
        return v;
    }
           
    //<value> ::= (<new> | <var>) { '.' <array> } [ '.' <int> ]
    private Value<?> procValue() throws IOException{
        Value <?> v = null;
        if(current.type == TokenType.NEW){
            v = procNew();
        }else if(current.type == TokenType.VAR){
            v = procVar();
        }else{
            showError();
        }
        
        while(current.type == TokenType.DOT){
            matchToken(TokenType.DOT);
            if(current.type == TokenType.SHOW ||
               current.type == TokenType.SORT ||
               current.type == TokenType.ADD ||
               current.type == TokenType.SET ||
               current.type == TokenType.FILTER ||
               current.type == TokenType.REMOVE ||
               current.type == TokenType.EACH ||
               current.type == TokenType.APPLY ){
               v = procArray(v);
            }else if(current.type == TokenType.AT || current.type == TokenType.SIZE ){
               v = procInt(v);
               break;
            }else{
                showError();
            }   
        }        
        return v;
    }
    
    //<new> ::= new (<nzerxo> | <nrand> | <nfill>)
    private ArrayValue procNew() throws IOException{
        ArrayValue av=null;
        matchToken(TokenType.NEW);
        if(current.type == TokenType.ZERO){
            av = procNzero();
        }else if(current.type == TokenType.RAND){
            av = procNrand();
        }else if(current.type == TokenType.FILL){
            av = procNfill();
        }else{
            showError();
        }
        return av;
    }
    
    //<nzero> ::= zero '[' <expr> ']' 
    private ZeroArrayValue procNzero() throws IOException{
        ZeroArrayValue za;
        matchToken(TokenType.ZERO);
        matchToken(TokenType.SBRA_OPEN);
        za = new ZeroArrayValue(procExpr(),lex.getLine());
        matchToken(TokenType.SBRA_CLOSE);
        return za;
    }
    
    //<nrand> ::= rand '[' <expr> ']' 
    private RandArrayValue procNrand() throws IOException{
        RandArrayValue ra;
        matchToken(TokenType.RAND);
        matchToken(TokenType.SBRA_OPEN);
        ra = new RandArrayValue(procExpr(),lex.getLine());
        matchToken(TokenType.SBRA_CLOSE);
        return ra;
    }
    
    //<nfill> ::= fill '[' <expr> ',' <expr> ']' 
    private FillArrayValue procNfill() throws IOException{
        FillArrayValue fa;
        matchToken(TokenType.FILL);
        matchToken(TokenType.SBRA_OPEN);
        Value<?> v1 = procExpr();
        matchToken(TokenType.COMMA);
        Value<?> v2 = procExpr();
        matchToken(TokenType.SBRA_CLOSE);
        fa = new FillArrayValue(v1,v2,lex.getLine());
        return fa;
    }
    
    //<array> ::= <show> | <sort> | <add> | <set> | <filter> | <remove> | <each> | <apply>
    private ArrayValue procArray(Value<?> v) throws IOException{
        if(current.type == TokenType.SHOW){
            return procShow(v);
        }else if(current.type == TokenType.SORT){
            return procSort(v);
        }else if(current.type == TokenType.ADD){
            return procAdd(v);
        }else if(current.type == TokenType.SET){
            return procSet(v);
        }else if(current.type == TokenType.FILTER){
            return procFilter(v);
        }else if(current.type == TokenType.REMOVE){
            return procRemove(v);
        }else if(current.type == TokenType.EACH){
            return procEach(v);
        }else if(current.type == TokenType.APPLY){
            return procApply(v);
        }else{
            showError();
            return null;
        }
    }
    
    //<show> ::= show '(' ')'
    private ShowArrayValue procShow(Value<?> v) throws IOException{
        matchToken(TokenType.SHOW);
        matchToken(TokenType.PAR_OPEN);
        matchToken(TokenType.PAR_CLOSE);
        ShowArrayValue sa = new ShowArrayValue(v,lex.getLine());
        return sa;
    }
    
    //<sort> ::= sort '(' ')'
    private SortArrayValue procSort(Value<?> v) throws IOException{
        matchToken(TokenType.SORT);
        matchToken(TokenType.PAR_OPEN);
        matchToken(TokenType.PAR_CLOSE);
        SortArrayValue sa = new SortArrayValue(v,lex.getLine());        
        return sa;
    }
    
    //<add> ::= add '(' <expr> ')'
    private AddArrayValue procAdd(Value<?> v) throws IOException{
        matchToken(TokenType.ADD);
        matchToken(TokenType.PAR_OPEN);
        AddArrayValue sa = new AddArrayValue(v,procExpr(),lex.getLine()); 
        matchToken(TokenType.PAR_CLOSE);
        return sa;
    }
    
    //<set> ::= set '(' <expr> ',' <expr> ')'
    private SetArrayValue procSet(Value<?> v) throws IOException{
        matchToken(TokenType.SET);
        matchToken(TokenType.PAR_OPEN);
        Value<?> index = procExpr();
        matchToken(TokenType.COMMA);
        Value<?> value = procExpr();
        matchToken(TokenType.PAR_CLOSE);
        return new SetArrayValue(v, index, value, lex.getLine());
    }
    
    //<filter> ::= filter '(' <var> '->' <boolexpr> ')'
    private FilterArrayValue procFilter(Value<?> v) throws IOException{
        matchToken(TokenType.FILTER);
        matchToken(TokenType.PAR_OPEN);
        Variable var = procVar();
        matchToken(TokenType.ARROW);
        BoolValue bool = procBoolExpr();
        matchToken(TokenType.PAR_CLOSE);
        return new FilterArrayValue(v, var, bool, lex.getLine());
    }
    
    //<remove> ::= remove '(' <var> '->' <boolexpr> ')'
    private RemoveArrayValue procRemove(Value<?> v) throws IOException{
        matchToken(TokenType.REMOVE);
        matchToken(TokenType.PAR_OPEN);
        Variable var = procVar();
        matchToken(TokenType.ARROW);
        BoolValue bool = procBoolExpr();
        matchToken(TokenType.PAR_CLOSE);
        return new RemoveArrayValue(v, var, bool, lex.getLine());
    }
    
    //<each> ::= each '(' <var> '->' <statements> ')'
    private EachArrayValue procEach(Value<?> v) throws IOException{
        matchToken(TokenType.EACH);
        matchToken(TokenType.PAR_OPEN);
        Variable var = procVar();
        matchToken(TokenType.ARROW);
        Command cmd = procStatements();
        matchToken(TokenType.PAR_CLOSE);
        return new EachArrayValue(v, var, cmd, lex.getLine());
    }
    
    //<apply> ::= apply '(' <var> '->' <statements> ')'
    private ApplyEachValue procApply(Value<?> v) throws IOException{
        matchToken(TokenType.APPLY);
        matchToken(TokenType.PAR_OPEN);
        Variable var = procVar();
        matchToken(TokenType.ARROW);
        Command cmd = procStatements();
        matchToken(TokenType.PAR_CLOSE);
        return new ApplyEachValue(v, var, cmd, lex.getLine());
    }
    
    //<int> ::= <at> | <size>
    private ArrayIntValue procInt(Value<?> v) throws IOException{
        ArrayIntValue ai = null;
        if(current.type == TokenType.AT){
            ai = procAt(v);
        }else if(current.type == TokenType.SIZE){
            ai = procSize(v);
        }else{
            showError();
        }
        return ai;
    }
    
    //<at> ::= at '(' <expr> ')'
    private AtArrayIntValue procAt(Value<?> v) throws IOException{
        matchToken(TokenType.AT);
        matchToken(TokenType.PAR_OPEN);
        AtArrayIntValue aa = new AtArrayIntValue(v,  procExpr(), lex.getLine());
        matchToken(TokenType.PAR_CLOSE);
        return aa;
    }
    
    //<size> ::= size '(' ')'
    private SizeArrayIntValue procSize(Value<?> v) throws IOException{
        matchToken(TokenType.SIZE);
        matchToken(TokenType.PAR_OPEN);
        matchToken(TokenType.PAR_CLOSE);
        return new SizeArrayIntValue(v,lex.getLine());
    }
    
    private Command procStatements() throws IOException{
        CommandBlock cb = new CommandBlock();
        Command c = procCmd();
        cb.addCommand(c);
        while(current.type == TokenType.PLUS ||
           current.type == TokenType.MINUS ||
           current.type == TokenType.NUMBER ||
           current.type == TokenType.LOAD ||
           current.type == TokenType.NEW ||
           current.type == TokenType.VAR ||
           current.type == TokenType.PAR_OPEN ||
           current.type == TokenType.PRINT || 
           current.type == TokenType.PRINTLN ||
           current.type == TokenType.IF ||
           current.type == TokenType.WHILE
            ){
            c = procCmd();
            cb.addCommand(c);
        }
        return cb;
    }
    
    //<cmd> ::= <assign> | <print> | <if> | <while>
    private Command procCmd() throws IOException{
        Command c = null;
        if(current.type == TokenType.PLUS ||
           current.type == TokenType.MINUS ||
           current.type == TokenType.NUMBER ||
           current.type == TokenType.LOAD ||
           current.type == TokenType.NEW ||
           current.type == TokenType.VAR ||
           current.type == TokenType.PAR_OPEN 
          ){
            c = procAssign();
        }else if(current.type == TokenType.PRINT || current.type == TokenType.PRINTLN){
            c = procPrint();
        }else if(current.type == TokenType.IF){
            c =procIf();
        }else if(current.type == TokenType.WHILE){
            c = procWhile();
        }else{
            showError();
        }
        
        return c;
    }
    
    private Variable procVar() throws IOException{
        String n = current.token;
        matchToken(TokenType.VAR);
        Variable v;
        if(!vars.containsKey(n)){
            v = new Variable(n);
            vars.put(n, v);
        }else{
            v = vars.get(n);
        }
        return v;
    }
    
    private ConstIntValue procNumber() throws IOException{
        int line = lex.getLine();
        int value = Integer.parseInt(current.token);
        matchToken(TokenType.NUMBER);
        ConstIntValue civ = new ConstIntValue(value, line);
        return civ;
    }
    
    private ConstStringValue procString() throws IOException{
        int line = lex.getLine();
        String text = current.token;
        matchToken(TokenType.STRING);
        ConstStringValue csv = new ConstStringValue(text, line);
        
        return csv;
    }
    
    public Command init() throws IOException{
        Command c = procStatements();
        matchToken(TokenType.END_OF_FILE);
        return c;
    }
}
