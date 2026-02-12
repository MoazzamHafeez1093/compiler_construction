/**
 * TokenType.java
 * Enum representing all possible token types in our custom language
 * 
 * @author CS4031 Assignment Group
 * @version Spring 2026
 */
public enum TokenType {
    // Keywords (must be checked before identifiers)
    KEYWORD,
    
    // Identifiers and Literals
    IDENTIFIER,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    STRING_LITERAL,
    CHAR_LITERAL,
    BOOLEAN_LITERAL,
    
    // Operators
    ARITHMETIC_OP,      // +, -, *, /, %, **
    RELATIONAL_OP,      // ==, !=, <, >, <=, >=
    LOGICAL_OP,         // &&, ||, !
    ASSIGNMENT_OP,      // =, +=, -=, *=, /=
    INCREMENT_OP,       // ++
    DECREMENT_OP,       // --
    
    // Punctuators
    PUNCTUATOR,         // ( ) { } [ ] , ; :
    
    // Comments (typically skipped, but tracked)
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,
    
    // Special
    WHITESPACE,         // spaces, tabs, newlines (usually skipped)
    ERROR,              // for malformed tokens
    EOF                 // end of file marker
}
