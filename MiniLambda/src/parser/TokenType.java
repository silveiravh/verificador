package parser;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    COMMA, // ,
    COLON, // :
    SEMI_COLON, // ;
    CBRA_OPEN, // {
    CBRA_CLOSE, // }
    SBRA_OPEN, // [
    SBRA_CLOSE, // ]
    QUOTATION_MARK, // "
    LAMBDA, // #

    // keywords
    AF, // af

    // others
    STATE, // state
    LETTER, // letter
};
