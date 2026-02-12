/**
 * Token.java
 * Represents a single token identified by the lexical analyzer
 * Contains all necessary information about the token's type, value, and location
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private int lineNumber;
    private int columnNumber;
    
    /**
     * Constructor to create a new token
     * @param type The category this token belongs to
     * @param lexeme The actual text/value of the token
     * @param lineNumber Line where token appears (1-indexed)
     * @param columnNumber Column where token starts (1-indexed)
     */
    public Token(TokenType type, String lexeme, int lineNumber, int columnNumber) {
        this.type = type;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    // Getters
    public TokenType getType() {
        return type;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public int getColumnNumber() {
        return columnNumber;
    }
    
    /**
     * Returns a formatted string representation of the token
     * Format: <TOKEN_TYPE, "lexeme", Line: X, Col: Y>
     */
    @Override
    public String toString() {
        return String.format("<%s, \"%s\", Line: %d, Col: %d>", 
                           type, lexeme, lineNumber, columnNumber);
    }
    
    /**
     * Returns a more detailed representation for debugging
     */
    public String toDetailedString() {
        return String.format("Token{type=%s, lexeme='%s', line=%d, col=%d}", 
                           type, lexeme, lineNumber, columnNumber);
    }
}
