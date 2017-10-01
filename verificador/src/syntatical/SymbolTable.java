/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntatical;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author silveira
 */
public class SymbolTable {

    private final Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<>();
        
        st.put("\"", TokenType.QUOTATION_MARK);
        st.put("{" , TokenType.CBRA_OPEN);
        st.put("}" ,TokenType.CBRA_CLOSE);
        st.put(":", TokenType.COLON);
        st.put("[", TokenType.SBRA_OPEN);
        st.put("]", TokenType.SBRA_CLOSE);
        st.put(",", TokenType.COMMA);
        
        st.put("af", TokenType.AF);
    }
}
