/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantical;

import static syntatical.Verificador.S;

/**
 *
 * @author silveira
 */
public class State {
    private String name;
    private Boolean i;
    private Boolean f;
    
    public State(String name) {
        this.name = name;
        this.i = false;
        this.f = false;
    }
    
    public static void setInicial(State state) {
        for(State s : S) {
            if(s.name.equals(state.name)) {
                s.i = true;
            }
        }
    }
    
    public static void setFinal(State state) {
        for(State s : S) {
            if(s.name.equals(state.name)) {
                s.f = true;
            }
        }
    }
}
