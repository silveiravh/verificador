/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import Semantical.Command;
import java.io.IOException;

/**
 *
 * @author aluno
 */
public class MiniLambda {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java mlambda [MiniLambda File]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            SyntaticalAnalysis s = new SyntaticalAnalysis((l));
            Command c = s.init();
            c.execute();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean checkType(TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                 type == TokenType.INVALID_TOKEN ||
                 type == TokenType.UNEXPECTED_EOF);
    }
    
}
