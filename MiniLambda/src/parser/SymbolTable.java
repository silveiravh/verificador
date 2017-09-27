package parser;

import java.util.Map;
import java.util.HashMap;

class SymbolTable {

    private final Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<>();
        st.put(",", TokenType.COMMA);
        st.put(":", TokenType.COLON);
        st.put(";", TokenType.SEMI_COLON);
        st.put("{" , TokenType.CBRA_OPEN);
        st.put("}" ,TokenType.CBRA_CLOSE);
        st.put("[", TokenType.SBRA_OPEN);
        st.put("]", TokenType.SBRA_CLOSE);
        st.put("#", TokenType.LAMBDA);
    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ?
            st.get(token) : TokenType.INVALID_TOKEN;
    }
}
