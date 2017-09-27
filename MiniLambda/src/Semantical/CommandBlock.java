/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantical;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aluno
 */
public class CommandBlock extends Command{
    private List<Command> commands;
    public CommandBlock(){
        super(-1);
        commands = new ArrayList<Command>();
    }

    public void addCommand(Command c){
        commands.add(c);
    }
    
    @Override
    public void execute() {
        for(Command c : commands){
            c.execute();
        }
    }
}
