# SimpleLang - Lexical Analyzer

**Course:** CS4031 - Compiler Construction  
**Assignment:** 01 Part 1 - Lexical Analyzer  
**Status:** ✅ Complete

## Overview

A hand-coded DFA-based lexical analyzer for SimpleLang (a custom programming language). Recognizes 18 token types with comprehensive error handling, symbol table tracking, and formatted statistics.

## Quick Start

```bash
cd src
javac *.java
java ManualScanner ../tests/test1.lang
```

## Language Specification

### Keywords (12 total)
`start` `finish` `loop` `condition` `declare` `output` `input` `function` `return` `break` `continue` `else`

### Identifiers
- Must start with uppercase letter: `A-Z`
- Followed by: lowercase letters, digits, underscores
- Max length: 31 characters
- Example: `Count`, `Total_sum`, `X_2024`

### Literals
- **Integer:** `42`, `-567`, `+100`
- **Float:** `3.14`, `1.5e10`, `2.0E-3` (1-6 decimal places)
- **String:** `"Hello"`, `"Line1\nLine2"` (supports: `\"`, `\\`, `\n`, `\t`, `\r`)
- **Character:** `'A'`, `'\n'`
- **Boolean:** `true`, `false`

### Operators
- **Arithmetic:** `+` `-` `*` `/` `%` `**`
- **Relational:** `==` `!=` `<` `>` `<=` `>=`
- **Logical:** `&&` `||` `!`
- **Assignment:** `=` `+=` `-=` `*=` `/=`
- **Increment/Decrement:** `++` `--`

### Comments
- **Single-line:** `## comment text`
- **Multi-line:** `#* comment text *#`

## Sample Program

```
start
    declare Count = 0
    declare Sum = 0
    
    loop (Count < 10)
        Sum = Sum + Count
        Count++
    finish
    
    output "Sum: ", Sum
finish
```

## Compilation & Execution

```bash
# Compile
cd src
javac *.java

# Run individual test
java ManualScanner ../tests/test1.lang

# Output includes: tokens, statistics, symbol table, errors
```

## Test Files

- `test1.lang` - All valid token types
- `test2.lang` - Complex expressions
- `test3.lang` - Strings with escape sequences
- `test4.lang` - Lexical error detection (~20+ errors)
- `test5.lang` - Comment processing

## Project Structure

```
├── src/
│   ├── TokenType.java       # Token type enumeration (18 types)
│   ├── Token.java            # Token class with position tracking
│   ├── SymbolTable.java      # Identifier tracking
│   ├── ErrorHandler.java     # Error detection & reporting
│   └── ManualScanner.java    # Main DFA-based scanner (810+ lines)
├── docs/
│   ├── Automata_Design.pdf   # NFA/DFA diagrams and transition tables
│   └── LanguageGrammar.txt   # Formal BNF grammar
├── tests/
│   ├── test1.lang through test5.lang
│   └── TestResults.txt       # Expected outputs
└── README.md                 # This file
```

## Implementation Details

**Features:**
- ✅ DFA-based token recognition
- ✅ All 18 token types recognized
- ✅ Longest match principle
- ✅ Accurate line & column tracking
- ✅ Comment removal (single/multi-line)
- ✅ Escape sequence support
- ✅ Scientific notation for floats
- ✅ Symbol table tracking
- ✅ Comprehensive error detection
- ✅ Error recovery (continues after errors)
- ✅ Formatted token output
- ✅ Statistics generation

## Running Tests

```bash
cd src

# All valid tokens (no errors)
java ManualScanner ../tests/test1.lang

# Complex expressions
java ManualScanner ../tests/test2.lang

# With errors
java ManualScanner ../tests/test4.lang

# Or all tests at once
cd ..
./run_all_tests.sh     # Linux/Mac
run_all_tests.bat      # Windows
```

## Team Members

| Name | Roll Number | Section |
|------|-------------|---------|
| Moazzam Hafeez | 22i-1093 | A |
| Fiza Jameel | 22i-0964 | A |

---

**Last Updated:** February 12, 2026
