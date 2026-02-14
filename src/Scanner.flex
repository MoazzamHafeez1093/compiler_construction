import java.util.*;

%%

%public
%class Yylex
%unicode
%line
%column
%type Token

/* ---------- MACROS ---------- */

DIGIT      = [0-9]
LETTER     = [A-Z]
LOWER      = [a-z]
ID         = {LETTER}({LOWER}|{DIGIT}|_){0,30}
INTEGER    = [+-]?{DIGIT}+
FLOAT      = [+-]?{DIGIT}+\.{DIGIT}{1,6}([eE][+-]?{DIGIT}+)?

%%

/* ---------- RULES ---------- */

/* 1. Multi-line comments */
"#*"([^*]|\*+[^*#])*\*+"#"     { /* skip */ }

/* 2. Single-line comments */
"##".*                         { /* skip */ }

/* 3. Multi-character operators */
"**"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }
"=="   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }
"!="   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }
"<="   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }
">="   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }
"&&"   { return new Token(TokenType.LOGICAL_OP, yytext(), yyline+1, yycolumn+1); }
"||"   { return new Token(TokenType.LOGICAL_OP, yytext(), yyline+1, yycolumn+1); }
"++"   { return new Token(TokenType.INCREMENT_OP, yytext(), yyline+1, yycolumn+1); }
"--"   { return new Token(TokenType.DECREMENT_OP, yytext(), yyline+1, yycolumn+1); }
"+="   { return new Token(TokenType.ASSIGNMENT_OP, yytext(), yyline+1, yycolumn+1); }
"-="   { return new Token(TokenType.ASSIGNMENT_OP, yytext(), yyline+1, yycolumn+1); }
"*="   { return new Token(TokenType.ASSIGNMENT_OP, yytext(), yyline+1, yycolumn+1); }
"/="   { return new Token(TokenType.ASSIGNMENT_OP, yytext(), yyline+1, yycolumn+1); }

/* 4. Keywords */
"start"     { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"finish"    { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"loop"      { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"condition" { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"declare"   { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"output"    { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"input"     { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"function"  { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"return"    { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"break"     { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"continue"  { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
"else"      { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }

/* 5. Boolean literals */
"true"  { return new Token(TokenType.BOOLEAN_LITERAL, yytext(), yyline+1, yycolumn+1); }
"false" { return new Token(TokenType.BOOLEAN_LITERAL, yytext(), yyline+1, yycolumn+1); }

/* 6. Identifier */
{ID}    { return new Token(TokenType.IDENTIFIER, yytext(), yyline+1, yycolumn+1); }

/* 7. Floating literals */
{FLOAT} { return new Token(TokenType.FLOAT_LITERAL, yytext(), yyline+1, yycolumn+1); }

/* 8. Integer literals */
{INTEGER} { return new Token(TokenType.INTEGER_LITERAL, yytext(), yyline+1, yycolumn+1); }

/* 9. String literal (correct escape handling) */
\"([^\"\\]|\\[\"\\ntr])*\" {
    return new Token(TokenType.STRING_LITERAL, yytext(), yyline+1, yycolumn+1);
}

/* 10. Character literal (strict) */
\'([^\'\\]|\\[\'\\ntr])\' {
    return new Token(TokenType.CHAR_LITERAL, yytext(), yyline+1, yycolumn+1);
}

/* 11. Single-character operators */
"="   { return new Token(TokenType.ASSIGNMENT_OP, yytext(), yyline+1, yycolumn+1); }
">"   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }
"<"   { return new Token(TokenType.RELATIONAL_OP, yytext(), yyline+1, yycolumn+1); }

"+"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }
"-"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }
"*"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }
"/"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }
"%"   { return new Token(TokenType.ARITHMETIC_OP, yytext(), yyline+1, yycolumn+1); }

"!"   { return new Token(TokenType.LOGICAL_OP, yytext(), yyline+1, yycolumn+1); }

/* 12. Punctuators */
[(){}\[\],;:] {
    return new Token(TokenType.PUNCTUATOR, yytext(), yyline+1, yycolumn+1);
}

/* Whitespace */
[ \t\r\n]+  { /* skip */ }

/* Invalid character */
. {
    System.out.println("Lexical Error at Line " + (yyline+1) +
        " Column " + (yycolumn+1) +
        " : Invalid character -> " + yytext());
}
