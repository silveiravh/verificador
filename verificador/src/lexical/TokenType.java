/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical;

/**
 *
 * @author silveira
 */
public enum TokenType {
    // special tokens
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,

    // symbols
    CBRA_OPEN,      // {
    CBRA_CLOSE,     // }
    COLON,          // :
    SBRA_OPEN,      // [
    SBRA_CLOSE,     // ]
    COMMA,          // ,

    // keywords
    AF,            // af

    // others
    STATE,         // state
    LETTER,        // letter
};
