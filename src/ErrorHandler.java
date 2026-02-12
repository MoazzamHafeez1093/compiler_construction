import java.util.*;

/**
 * ErrorHandler.java
 * Handles detection, reporting, and recovery from lexical errors
 * Supports continuing scanning after errors to find all issues
 */
public class ErrorHandler {
    
    /**
     * Inner class to represent a lexical error
     */
    private class LexicalError {
        String errorType;
        int lineNumber;
        int columnNumber;
        String lexeme;
        String reason;
        
        LexicalError(String errorType, int line, int col, String lexeme, String reason) {
            this.errorType = errorType;
            this.lineNumber = line;
            this.columnNumber = col;
            this.lexeme = lexeme;
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return String.format("ERROR [%s] at Line: %d, Col: %d - Lexeme: '%s' - %s",
                               errorType, lineNumber, columnNumber, lexeme, reason);
        }
    }
    
    private List<LexicalError> errors;
    private boolean hasErrors;
    
    public ErrorHandler() {
        errors = new ArrayList<>();
        hasErrors = false;
    }
    
    /**
     * Report an invalid character error
     */
    public void reportInvalidCharacter(char ch, int line, int col) {
        String lexeme = String.valueOf(ch);
        addError("INVALID_CHARACTER", line, col, lexeme, 
                "Character '" + ch + "' is not recognized in the language");
    }
    
    /**
     * Report a malformed number literal error
     */
    public void reportMalformedNumber(String lexeme, int line, int col, String issue) {
        addError("MALFORMED_NUMBER", line, col, lexeme, issue);
    }
    
    /**
     * Report an invalid identifier error
     */
    public void reportInvalidIdentifier(String lexeme, int line, int col, String issue) {
        addError("INVALID_IDENTIFIER", line, col, lexeme, issue);
    }
    
    /**
     * Report an unterminated string literal error
     */
    public void reportUnterminatedString(String lexeme, int line, int col) {
        addError("UNTERMINATED_STRING", line, col, lexeme, 
                "String literal not properly closed with \"");
    }
    
    /**
     * Report an unterminated character literal error
     */
    public void reportUnterminatedChar(String lexeme, int line, int col) {
        addError("UNTERMINATED_CHAR", line, col, lexeme, 
                "Character literal not properly closed with '");
    }
    
    /**
     * Report an unclosed multi-line comment error
     */
    public void reportUnclosedComment(int startLine, int startCol) {
        addError("UNCLOSED_COMMENT", startLine, startCol, "#*", 
                "Multi-line comment started but never closed with *#");
    }
    
    /**
     * Report an invalid escape sequence in string/char
     */
    public void reportInvalidEscape(String sequence, int line, int col) {
        addError("INVALID_ESCAPE", line, col, sequence, 
                "Invalid escape sequence. Valid escapes: \\n, \\t, \\r, \\\", \\', \\\\");
    }
    
    /**
     * Add a custom error message
     */
    public void addError(String type, int line, int col, String lexeme, String reason) {
        errors.add(new LexicalError(type, line, col, lexeme, reason));
        hasErrors = true;
    }
    
    /**
     * Check if any errors have been recorded
     */
    public boolean hasErrors() {
        return hasErrors;
    }
    
    /**
     * Get the total number of errors
     */
    public int getErrorCount() {
        return errors.size();
    }
    
    /**
     * Display all errors in a formatted report
     */
    public void displayErrors() {
        if (!hasErrors) {
            System.out.println("\n✓ No lexical errors found!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("LEXICAL ERROR REPORT");
        System.out.println("=".repeat(80));
        System.out.println("Total errors found: " + errors.size());
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < errors.size(); i++) {
            System.out.println((i + 1) + ". " + errors.get(i));
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    /**
     * Clear all recorded errors (useful for rescanning)
     */
    public void clearErrors() {
        errors.clear();
        hasErrors = false;
    }
    
    /**
     * Get all error messages as a list (useful for testing)
     */
    public List<String> getErrorMessages() {
        List<String> messages = new ArrayList<>();
        for (LexicalError error : errors) {
            messages.add(error.toString());
        }
        return messages;
    }
}
