import java.io.*;
import java.util.*;

/**
 * ManualScanner.java
 * A hand-coded lexical analyzer for our custom programming language
 * Implements DFA-based token recognition with proper error handling
 * 
 * Features:
 * - Pattern matching following priority rules
 * - Longest match principle
 * - Line/column tracking
 * - Symbol table management
 * - Comprehensive error handling and recovery
 */
public class ManualScanner {
    
    // Input handling
    private String sourceCode;
    private int position;           // current position in source
    private int length;             // total length of source
    
    // Position tracking
    private int lineNumber;         // current line (1-indexed)
    private int columnNumber;       // current column (1-indexed)
    private int tokenStartLine;     // line where current token started
    private int tokenStartColumn;   // column where current token started
    
    // Output collections
    private List<Token> tokens;
    private SymbolTable symbolTable;
    private ErrorHandler errorHandler;
    
    // Statistics
    private Map<TokenType, Integer> tokenCounts;
    private int commentCount;
    
    // Keywords set for quick lookup
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "start", "finish", "loop", "condition", "declare", "output", 
        "input", "function", "return", "break", "continue", "else"
    ));
    
    /**
     * Constructor initializes the scanner with source code
     */
    public ManualScanner(String sourceCode) {
        this.sourceCode = sourceCode;
        this.length = sourceCode.length();
        this.position = 0;
        this.lineNumber = 1;
        this.columnNumber = 1;
        
        this.tokens = new ArrayList<>();
        this.symbolTable = new SymbolTable();
        this.errorHandler = new ErrorHandler();
        this.tokenCounts = new HashMap<>();
        this.commentCount = 0;
    }
    
    /**
     * Main scanning method - processes entire source code
     */
    public void scan() {
        while (position < length) {
            tokenStartLine = lineNumber;
            tokenStartColumn = columnNumber;
            
            Token token = getNextToken();
            
            if (token != null) {
                // Don't add whitespace or comment tokens to output
                // but do count comments for statistics
                if (token.getType() == TokenType.SINGLE_LINE_COMMENT || 
                    token.getType() == TokenType.MULTI_LINE_COMMENT) {
                    commentCount++;
                } else if (token.getType() != TokenType.WHITESPACE) {
                    tokens.add(token);
                    
                    // Update token counts
                    tokenCounts.put(token.getType(), 
                                  tokenCounts.getOrDefault(token.getType(), 0) + 1);
                    
                    // Add identifiers to symbol table
                    if (token.getType() == TokenType.IDENTIFIER) {
                        symbolTable.addIdentifier(token.getLexeme(), 
                                                token.getLineNumber(), 
                                                token.getColumnNumber());
                    }
                }
            }
        }
        
        // Add EOF token
        Token eofToken = new Token(TokenType.EOF, "", lineNumber, columnNumber);
        tokens.add(eofToken);
    }
    
    /**
     * Get the next token from the source code
     * Implements pattern matching priority
     */
    private Token getNextToken() {
        if (position >= length) {
            return null;
        }
        
        char current = sourceCode.charAt(position);
        
        // Priority 1: Multi-line comments #* ... *#
        if (current == '#' && peek(1) == '*') {
            return scanMultiLineComment();
        }
        
        // Priority 2: Single-line comments ##
        if (current == '#' && peek(1) == '#') {
            return scanSingleLineComment();
        }
        
        // Priority 3: Multi-character operators
        Token opToken = tryMultiCharOperator();
        if (opToken != null) {
            return opToken;
        }
        
        // Priority 4-5: Check for keywords and booleans (lowercase)
        if (isLowercase(current)) {
            // Try to match keywords first
            if (matchWord("start") || matchWord("finish") || matchWord("loop") || 
                matchWord("condition") || matchWord("declare") || matchWord("output") ||
                matchWord("input") || matchWord("function") || matchWord("return") ||
                matchWord("break") || matchWord("continue") || matchWord("else")) {
                return scanKeyword();
            }
            // Try to match boolean literals
            if (matchWord("true") || matchWord("false")) {
                return scanBooleanLiteral();
            }
            // If lowercase but not a keyword or boolean, it's an error
            errorHandler.reportInvalidCharacter(current, lineNumber, columnNumber);
            advance();
            return getNextToken();
        }
        
        // Priority 6: Identifiers (uppercase start)
        if (isUppercase(current)) {
            return scanIdentifier();
        }
        
        // Priority 7: Floating-point literals (must check before integers)
        if (isDigit(current) || (current == '+' || current == '-') && isDigit(peek(1))) {
            // Look ahead to see if it's a float
            int tempPos = position;
            if (current == '+' || current == '-') tempPos++;
            
            // Check if there's a decimal point ahead
            while (tempPos < length && isDigit(sourceCode.charAt(tempPos))) {
                tempPos++;
            }
            
            if (tempPos < length && sourceCode.charAt(tempPos) == '.') {
                return scanFloatLiteral();
            } else {
                return scanIntegerLiteral();
            }
        }
        
        // Priority 8: String literals
        if (current == '"') {
            return scanStringLiteral();
        }
        
        // Priority 9: Character literals
        if (current == '\'') {
            return scanCharLiteral();
        }
        
        // Priority 10: Single-character operators
        if (isOperatorChar(current)) {
            return scanSingleCharOperator();
        }
        
        // Priority 11: Punctuators
        if (isPunctuator(current)) {
            return scanPunctuator();
        }
        
        // Priority 12: Whitespace
        if (isWhitespace(current)) {
            return scanWhitespace();
        }
        
        // If we reach here, it's an invalid character
        errorHandler.reportInvalidCharacter(current, lineNumber, columnNumber);
        advance(); // Skip the invalid character and continue
        return getNextToken(); // Try to get the next token
    }
    
    /**
     * Scan multi-line comment: #* ... *#
     */
    private Token scanMultiLineComment() {
        StringBuilder lexeme = new StringBuilder();
        int startLine = lineNumber;
        int startCol = columnNumber;
        
        lexeme.append(consume()); // #
        lexeme.append(consume()); // *
        
        boolean closed = false;
        
        while (position < length) {
            char current = sourceCode.charAt(position);
            
            if (current == '*' && peek(1) == '#') {
                lexeme.append(consume()); // *
                lexeme.append(consume()); // #
                closed = true;
                break;
            }
            
            lexeme.append(consume());
        }
        
        if (!closed) {
            errorHandler.reportUnclosedComment(startLine, startCol);
        }
        
        return new Token(TokenType.MULTI_LINE_COMMENT, lexeme.toString(), 
                       startLine, startCol);
    }
    
    /**
     * Scan single-line comment: ## ...
     */
    private Token scanSingleLineComment() {
        StringBuilder lexeme = new StringBuilder();
        int startLine = lineNumber;
        int startCol = columnNumber;
        
        lexeme.append(consume()); // #
        lexeme.append(consume()); // #
        
        // Read until end of line or end of file
        while (position < length && sourceCode.charAt(position) != '\n') {
            lexeme.append(consume());
        }
        
        return new Token(TokenType.SINGLE_LINE_COMMENT, lexeme.toString(), 
                       startLine, startCol);
    }
    
    /**
     * Try to match multi-character operators
     * Returns null if no match found
     */
    private Token tryMultiCharOperator() {
        char current = sourceCode.charAt(position);
        char next = peek(1);
        
        // Two-character operators
        String twoChar = "" + current + next;
        
        switch (twoChar) {
            case "**":
                return createOpToken(twoChar, TokenType.ARITHMETIC_OP);
            case "==":
            case "!=":
            case "<=":
            case ">=":
                return createOpToken(twoChar, TokenType.RELATIONAL_OP);
            case "&&":
            case "||":
                return createOpToken(twoChar, TokenType.LOGICAL_OP);
            case "++":
                return createOpToken(twoChar, TokenType.INCREMENT_OP);
            case "--":
                return createOpToken(twoChar, TokenType.DECREMENT_OP);
            case "+=":
            case "-=":
            case "*=":
            case "/=":
                return createOpToken(twoChar, TokenType.ASSIGNMENT_OP);
        }
        
        return null;
    }
    
    /**
     * Helper to create operator token and advance position
     */
    private Token createOpToken(String op, TokenType type) {
        int startLine = lineNumber;
        int startCol = columnNumber;
        for (int i = 0; i < op.length(); i++) {
            consume();
        }
        return new Token(type, op, startLine, startCol);
    }
    
    /**
     * Scan boolean literal: true or false
     */
    private Token scanBooleanLiteral() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        while (position < length && isLetter(sourceCode.charAt(position))) {
            lexeme.append(consume());
        }
        
        return new Token(TokenType.BOOLEAN_LITERAL, lexeme.toString(), 
                       lineNumber, startCol);
    }
    
    /**
     * Scan keyword
     */
    private Token scanKeyword() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        while (position < length && isLetter(sourceCode.charAt(position))) {
            lexeme.append(consume());
        }
        
        return new Token(TokenType.KEYWORD, lexeme.toString(), lineNumber, startCol);
    }
    
    /**
     * Scan identifier: [A-Z][a-z0-9_]{0,30}
     * Also checks if it's a keyword
     */
    private Token scanIdentifier() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        // First character must be uppercase
        lexeme.append(consume());
        
        // Subsequent characters can be lowercase, digits, or underscores
        while (position < length) {
            char ch = sourceCode.charAt(position);
            if (isLowercase(ch) || isDigit(ch) || ch == '_') {
                lexeme.append(consume());
            } else {
                break;
            }
        }
        
        String identifier = lexeme.toString();
        
        // Check length constraint
        if (identifier.length() > 31) {
            errorHandler.reportInvalidIdentifier(identifier, lineNumber, startCol,
                "Identifier exceeds maximum length of 31 characters");
        }
        
        // Check if it's a keyword
        if (KEYWORDS.contains(identifier)) {
            return new Token(TokenType.KEYWORD, identifier, lineNumber, startCol);
        }
        
        return new Token(TokenType.IDENTIFIER, identifier, lineNumber, startCol);
    }
    
    /**
     * Scan integer literal: [+-]?[0-9]+
     */
    private Token scanIntegerLiteral() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        // Optional sign
        char current = sourceCode.charAt(position);
        if (current == '+' || current == '-') {
            lexeme.append(consume());
        }
        
        // Digits
        if (!isDigit(sourceCode.charAt(position))) {
            errorHandler.reportMalformedNumber(lexeme.toString(), lineNumber, startCol,
                "Expected digit after sign");
            return new Token(TokenType.ERROR, lexeme.toString(), lineNumber, startCol);
        }
        
        while (position < length && isDigit(sourceCode.charAt(position))) {
            lexeme.append(consume());
        }
        
        return new Token(TokenType.INTEGER_LITERAL, lexeme.toString(), 
                       lineNumber, startCol);
    }
    
    /**
     * Scan floating-point literal: [+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?
     */
    private Token scanFloatLiteral() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        // Optional sign
        char current = sourceCode.charAt(position);
        if (current == '+' || current == '-') {
            lexeme.append(consume());
        }
        
        // Integer part
        while (position < length && isDigit(sourceCode.charAt(position))) {
            lexeme.append(consume());
        }
        
        // Decimal point
        if (position < length && sourceCode.charAt(position) == '.') {
            lexeme.append(consume());
        } else {
            errorHandler.reportMalformedNumber(lexeme.toString(), lineNumber, startCol,
                "Expected decimal point for floating-point literal");
            return new Token(TokenType.ERROR, lexeme.toString(), lineNumber, startCol);
        }
        
        // Fractional part (1-6 digits)
        int decimalDigits = 0;
        while (position < length && isDigit(sourceCode.charAt(position))) {
            lexeme.append(consume());
            decimalDigits++;
        }
        
        if (decimalDigits == 0) {
            errorHandler.reportMalformedNumber(lexeme.toString(), lineNumber, startCol,
                "Missing fractional part after decimal point");
        } else if (decimalDigits > 6) {
            errorHandler.reportMalformedNumber(lexeme.toString(), lineNumber, startCol,
                "Too many decimal digits (maximum 6 allowed)");
        }
        
        // Optional exponent
        if (position < length && (sourceCode.charAt(position) == 'e' || 
                                  sourceCode.charAt(position) == 'E')) {
            lexeme.append(consume());
            
            // Optional sign in exponent
            if (position < length && (sourceCode.charAt(position) == '+' || 
                                     sourceCode.charAt(position) == '-')) {
                lexeme.append(consume());
            }
            
            // Exponent digits
            int expDigits = 0;
            while (position < length && isDigit(sourceCode.charAt(position))) {
                lexeme.append(consume());
                expDigits++;
            }
            
            if (expDigits == 0) {
                errorHandler.reportMalformedNumber(lexeme.toString(), lineNumber, startCol,
                    "Missing exponent digits after 'e' or 'E'");
            }
        }
        
        return new Token(TokenType.FLOAT_LITERAL, lexeme.toString(), 
                       lineNumber, startCol);
    }
    
    /**
     * Scan string literal: "..."
     * Supports escape sequences: \", \\, \n, \t, \r
     */
    private Token scanStringLiteral() {
        StringBuilder lexeme = new StringBuilder();
        int startLine = lineNumber;
        int startCol = columnNumber;
        
        lexeme.append(consume()); // opening "
        
        boolean closed = false;
        
        while (position < length) {
            char ch = sourceCode.charAt(position);
            
            if (ch == '\n') {
                // Newline in string - error
                errorHandler.reportUnterminatedString(lexeme.toString(), 
                                                     startLine, startCol);
                break;
            }
            
            if (ch == '"') {
                lexeme.append(consume());
                closed = true;
                break;
            }
            
            if (ch == '\\') {
                lexeme.append(consume()); // backslash
                if (position < length) {
                    char escaped = sourceCode.charAt(position);
                    if (escaped == '"' || escaped == '\\' || escaped == 'n' || 
                        escaped == 't' || escaped == 'r') {
                        lexeme.append(consume());
                    } else {
                        errorHandler.reportInvalidEscape("\\" + escaped, 
                                                        lineNumber, columnNumber);
                        consume(); // skip the invalid escape
                    }
                }
            } else {
                lexeme.append(consume());
            }
        }
        
        if (!closed) {
            errorHandler.reportUnterminatedString(lexeme.toString(), 
                                                 startLine, startCol);
        }
        
        return new Token(TokenType.STRING_LITERAL, lexeme.toString(), 
                       startLine, startCol);
    }
    
    /**
     * Scan character literal: '.'
     * Supports escape sequences: \', \\, \n, \t, \r
     */
    private Token scanCharLiteral() {
        StringBuilder lexeme = new StringBuilder();
        int startLine = lineNumber;
        int startCol = columnNumber;
        
        lexeme.append(consume()); // opening '
        
        boolean closed = false;
        int charCount = 0;
        
        while (position < length && charCount < 3) {
            char ch = sourceCode.charAt(position);
            
            if (ch == '\n') {
                errorHandler.reportUnterminatedChar(lexeme.toString(), 
                                                   startLine, startCol);
                break;
            }
            
            if (ch == '\'') {
                lexeme.append(consume());
                closed = true;
                break;
            }
            
            if (ch == '\\') {
                lexeme.append(consume());
                charCount++;
                if (position < length) {
                    char escaped = sourceCode.charAt(position);
                    if (escaped == '\'' || escaped == '\\' || escaped == 'n' || 
                        escaped == 't' || escaped == 'r') {
                        lexeme.append(consume());
                    } else {
                        errorHandler.reportInvalidEscape("\\" + escaped, 
                                                        lineNumber, columnNumber);
                        consume();
                    }
                }
            } else {
                lexeme.append(consume());
                charCount++;
            }
        }
        
        if (!closed) {
            errorHandler.reportUnterminatedChar(lexeme.toString(), 
                                               startLine, startCol);
        }
        
        return new Token(TokenType.CHAR_LITERAL, lexeme.toString(), 
                       startLine, startCol);
    }
    
    /**
     * Scan single-character operator
     */
    private Token scanSingleCharOperator() {
        char ch = consume();
        TokenType type;
        
        switch (ch) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
                type = TokenType.ARITHMETIC_OP;
                break;
            case '<':
            case '>':
                type = TokenType.RELATIONAL_OP;
                break;
            case '!':
                type = TokenType.LOGICAL_OP;
                break;
            case '=':
                type = TokenType.ASSIGNMENT_OP;
                break;
            default:
                type = TokenType.ERROR;
        }
        
        return new Token(type, String.valueOf(ch), lineNumber, 
                       columnNumber - 1);
    }
    
    /**
     * Scan punctuator
     */
    private Token scanPunctuator() {
        char ch = consume();
        return new Token(TokenType.PUNCTUATOR, String.valueOf(ch), 
                       lineNumber, columnNumber - 1);
    }
    
    /**
     * Scan whitespace (spaces, tabs, newlines)
     */
    private Token scanWhitespace() {
        StringBuilder lexeme = new StringBuilder();
        int startCol = columnNumber;
        
        while (position < length && isWhitespace(sourceCode.charAt(position))) {
            lexeme.append(consume());
        }
        
        return new Token(TokenType.WHITESPACE, lexeme.toString(), 
                       lineNumber, startCol);
    }
    
    // ==================== Helper Methods ====================
    
    /**
     * Advance position and update line/column numbers
     * Returns the character that was consumed
     */
    private char consume() {
        char ch = sourceCode.charAt(position);
        position++;
        
        if (ch == '\n') {
            lineNumber++;
            columnNumber = 1;
        } else {
            columnNumber++;
        }
        
        return ch;
    }
    
    /**
     * Look ahead at character at offset from current position
     */
    private char peek(int offset) {
        int pos = position + offset;
        if (pos < length) {
            return sourceCode.charAt(pos);
        }
        return '\0';
    }
    
    /**
     * Advance position without returning character
     */
    private void advance() {
        if (position < length) {
            consume();
        }
    }
    
    /**
     * Check if a word matches at current position
     */
    private boolean matchWord(String word) {
        if (position + word.length() > length) {
            return false;
        }
        
        String substring = sourceCode.substring(position, position + word.length());
        
        if (!substring.equals(word)) {
            return false;
        }
        
        // Check that it's not part of a larger identifier
        if (position + word.length() < length) {
            char nextChar = sourceCode.charAt(position + word.length());
            if (isLetter(nextChar) || isDigit(nextChar) || nextChar == '_') {
                return false;
            }
        }
        
        return true;
    }
    
    // Character classification methods
    private boolean isUppercase(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }
    
    private boolean isLowercase(char ch) {
        return ch >= 'a' && ch <= 'z';
    }
    
    private boolean isLetter(char ch) {
        return isUppercase(ch) || isLowercase(ch);
    }
    
    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    private boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }
    
    private boolean isOperatorChar(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' ||
               ch == '=' || ch == '<' || ch == '>' || ch == '!' || ch == '&' || ch == '|';
    }
    
    private boolean isPunctuator(char ch) {
        return ch == '(' || ch == ')' || ch == '{' || ch == '}' ||
               ch == '[' || ch == ']' || ch == ',' || ch == ';' || ch == ':';
    }
    
    // ==================== Output Methods ====================
    
    /**
     * Display all tokens in required format
     */
    public void displayTokens() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TOKENS");
        System.out.println("=".repeat(80));
        
        for (Token token : tokens) {
            if (token.getType() != TokenType.EOF) {
                System.out.println(token);
            }
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    /**
     * Display scanning statistics
     */
    public void displayStatistics() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCANNING STATISTICS");
        System.out.println("=".repeat(80));
        
        System.out.println("Total tokens (excluding whitespace & comments): " + 
                         (tokens.size() - 1)); // -1 for EOF
        System.out.println("Lines processed: " + lineNumber);
        System.out.println("Comments removed: " + commentCount);
        
        System.out.println("\nToken count by type:");
        System.out.println("-".repeat(50));
        
        // Sort token types for consistent output
        List<Map.Entry<TokenType, Integer>> sortedCounts = 
            new ArrayList<>(tokenCounts.entrySet());
        sortedCounts.sort((a, b) -> a.getKey().toString().compareTo(b.getKey().toString()));
        
        for (Map.Entry<TokenType, Integer> entry : sortedCounts) {
            System.out.printf("  %-25s: %d%n", entry.getKey(), entry.getValue());
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
    
    /**
     * Get tokens list (for external use)
     */
    public List<Token> getTokens() {
        return tokens;
    }
    
    /**
     * Get symbol table (for external use)
     */
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    /**
     * Get error handler (for external use)
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    // ==================== Main Method ====================
    
    /**
     * Main method for testing the scanner
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ManualScanner <source-file>");
            System.out.println("Example: java ManualScanner test1.lang");
            return;
        }
        
        String filename = args[0];
        
        try {
            // Read the source file
            String sourceCode = readFile(filename);
            
            System.out.println("Scanning file: " + filename);
            System.out.println("=".repeat(80));
            
            // Create scanner and process
            ManualScanner scanner = new ManualScanner(sourceCode);
            scanner.scan();
            
            // Display results
            scanner.displayTokens();
            scanner.displayStatistics();
            scanner.getSymbolTable().display();
            
            // Display any errors found
            scanner.getErrorHandler().displayErrors();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to read entire file into string
     */
    private static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        }
        return content.toString();
    }
}
