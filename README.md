# Custom Programming Language - Lexical Analyzer

**Course:** CS4031 - Compiler Construction  
**Assignment:** 01 - Lexical Analyzer Implementation  
**Semester:** Spring 2026

## Table of Contents
1. [Language Overview](#language-overview)
2. [Keywords](#keywords)
3. [Identifiers](#identifiers)
4. [Literals](#literals)
5. [Operators](#operators)
6. [Comments](#comments)
7. [Sample Programs](#sample-programs)
8. [Compilation and Execution](#compilation-and-execution)
9. [Team Members](#team-members)

---

## Language Overview

**Language Name:** SimpleLang  
**File Extension:** `.lang`

SimpleLang is a custom imperative programming language designed for educational purposes in compiler construction. It features structured programming constructs, type declarations, and common operators found in mainstream languages.

---

## Keywords

Our language includes 12 reserved keywords (all case-sensitive):

| Keyword | Meaning | Usage Example |
|---------|---------|---------------|
| `start` | Begin a program or code block | `start ... finish` |
| `finish` | End a program or code block | `start ... finish` |
| `loop` | Begin a loop structure | `loop (condition) ... finish` |
| `condition` | Conditional statement (if) | `condition (x > 0) ... finish` |
| `else` | Alternative branch | `condition (...) ... else ... finish` |
| `declare` | Variable declaration | `declare Count = 0` |
| `function` | Function definition | `function Add(A, B) ... finish` |
| `return` | Return from function | `return Result` |
| `input` | Read input | `input Value` |
| `output` | Display output | `output "Hello"` |
| `break` | Exit loop early | `break` |
| `continue` | Skip to next iteration | `continue` |

**Important:** Keywords are case-sensitive and must be written exactly as shown.

---

## Identifiers

### Rules
1. **Must start with an uppercase letter (A-Z)**
2. Can be followed by lowercase letters, digits, or underscores
3. Maximum length: 31 characters
4. Case-sensitive

### Valid Examples
```
Count
Total_sum
Variable_name_123
X
Result_2024
```

### Invalid Examples
```
count              // starts with lowercase
myVariable         // starts with lowercase
2Count             // starts with digit
_Variable          // starts with underscore
```

---

## Literals

### Integer Literals
**Format:** Optional sign followed by one or more digits

```
42          // positive integer
-567        // negative integer
+100        // explicit positive
0           // zero
```

**Invalid:**
```
12.34       // use float instead
1,000       // no comma separators
```

### Floating-Point Literals
**Format:** `[+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?`
- Optional sign
- Integer part
- Decimal point
- 1-6 decimal digits (required)
- Optional scientific notation

```
3.14        // basic float
-0.123456   // negative with max decimals
+2.5        // explicit positive
1.5e10      // scientific notation (1.5 × 10¹⁰)
2.0E-3      // scientific notation (2.0 × 10⁻³)
```

**Invalid:**
```
3.          // missing fractional part
.14         // missing integer part
1.2345678   // more than 6 decimal digits
```

### String Literals
**Format:** Text enclosed in double quotes with escape sequence support

**Escape Sequences:**
- `\"` - Double quote
- `\\` - Backslash
- `\n` - Newline
- `\t` - Tab
- `\r` - Carriage return

```
"Hello, World!"
"He said \"Hi\""
"C:\\Program Files\\App"
"Line 1\nLine 2"
```

### Character Literals
**Format:** Single character enclosed in single quotes

```
'A'
'z'
'0'
'\n'        // newline character
'\''        // single quote character
```

### Boolean Literals
**Format:** `true` or `false` (case-sensitive)

```
declare Flag = true
declare Is_valid = false
```

---

## Operators

### Arithmetic Operators
| Operator | Description | Precedence | Example |
|----------|-------------|------------|---------|
| `**` | Exponentiation | Highest | `X ** 2` |
| `*` | Multiplication | High | `X * Y` |
| `/` | Division | High | `X / Y` |
| `%` | Modulus | High | `X % Y` |
| `+` | Addition | Medium | `X + Y` |
| `-` | Subtraction | Medium | `X - Y` |

### Relational Operators
| Operator | Description | Example |
|----------|-------------|---------|
| `==` | Equal to | `X == Y` |
| `!=` | Not equal to | `X != Y` |
| `<` | Less than | `X < Y` |
| `>` | Greater than | `X > Y` |
| `<=` | Less than or equal | `X <= Y` |
| `>=` | Greater than or equal | `X >= Y` |

### Logical Operators
| Operator | Description | Example |
|----------|-------------|---------|
| `&&` | Logical AND | `X > 0 && Y > 0` |
| `\|\|` | Logical OR | `X > 0 \|\| Y > 0` |
| `!` | Logical NOT | `!Flag` |

### Assignment Operators
| Operator | Description | Example | Equivalent |
|----------|-------------|---------|------------|
| `=` | Simple assignment | `X = 10` | - |
| `+=` | Add and assign | `X += 5` | `X = X + 5` |
| `-=` | Subtract and assign | `X -= 3` | `X = X - 3` |
| `*=` | Multiply and assign | `X *= 2` | `X = X * 2` |
| `/=` | Divide and assign | `X /= 4` | `X = X / 4` |

### Increment/Decrement Operators
| Operator | Description | Example |
|----------|-------------|---------|
| `++` | Increment | `Count++` |
| `--` | Decrement | `Count--` |

### Operator Precedence (Highest to Lowest)
1. `**` (Exponentiation)
2. `*`, `/`, `%` (Multiplicative)
3. `+`, `-` (Additive)
4. `<`, `>`, `<=`, `>=` (Relational)
5. `==`, `!=` (Equality)
6. `&&` (Logical AND)
7. `||` (Logical OR)
8. `=`, `+=`, `-=`, `*=`, `/=` (Assignment)

---

## Comments

### Single-Line Comments
Start with `##` and continue to the end of the line.

```
## This is a single-line comment
declare X = 10  ## Comment after code
```

### Multi-Line Comments
Enclosed between `#*` and `*#`.

```
#*
   This is a multi-line comment
   It can span multiple lines
   Useful for documentation
*#
```

---

## Sample Programs

### Sample Program 1: Basic Arithmetic
```
## Program: Simple Calculator
start
    declare Num1 = 10
    declare Num2 = 5
    declare Sum = 0
    declare Product = 0
    
    Sum = Num1 + Num2
    Product = Num1 * Num2
    
    output "Sum: ", Sum
    output "Product: ", Product
finish
```

### Sample Program 2: Conditional Logic
```
## Program: Grade Checker
start
    declare Score = 85
    declare Grade = 'B'
    
    condition (Score >= 90)
        Grade = 'A'
        output "Excellent!"
    else
        condition (Score >= 80)
            Grade = 'B'
            output "Very Good!"
        else
            condition (Score >= 70)
                Grade = 'C'
                output "Good"
            else
                Grade = 'F'
                output "Need Improvement"
            finish
        finish
    finish
    
    output "Your grade is: ", Grade
finish
```

### Sample Program 3: Loops and Functions
```
## Program: Factorial Calculator
start function Factorial(Number)
    declare Result = 1
    declare Counter = 1
    
    loop (Counter <= Number)
        Result = Result * Counter
        Counter++
    finish
    
    return Result
finish

start
    declare Num = 5
    declare Fact = 0
    
    output "Calculating factorial of ", Num
    Fact = Factorial(Num)
    output "Result: ", Fact
    
    ## Output: Result: 120
finish
```

### Sample Program 4: Array Operations
```
## Program: Array Sum
start
    declare Numbers[10]
    declare Index = 0
    declare Sum = 0
    
    ## Initialize array
    loop (Index < 10)
        Numbers[Index] = Index * 2
        Index++
    finish
    
    ## Calculate sum
    Index = 0
    loop (Index < 10)
        Sum = Sum + Numbers[Index]
        Index++
    finish
    
    output "Sum of array elements: ", Sum
finish
```

### Sample Program 5: String Operations
```
## Program: String Processing
start
    declare Message = "Hello, SimpleLang!"
    declare Line_1 = "First line"
    declare Line_2 = "Second line"
    declare Combined = ""
    
    output Message
    output Line_1, "\n", Line_2
    
    declare Path = "C:\\Users\\Student\\Documents\\code.lang"
    output "File path: ", Path
    
    declare Quote = "Einstein said, \"Imagination is more important than knowledge.\""
    output Quote
finish
```

---

## Compilation and Execution

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Text editor or IDE

### Compiling the Scanner

Navigate to the `src` directory and compile all Java files:

```bash
cd src
javac TokenType.java Token.java SymbolTable.java ErrorHandler.java ManualScanner.java
```

### Running the Scanner

Execute the scanner with a source file:

```bash
java ManualScanner ../tests/test1.lang
```

### Expected Output

The scanner will produce:
1. **Token List** - All recognized tokens with their types and positions
2. **Statistics** - Token counts, lines processed, comments removed
3. **Symbol Table** - All identifiers with their first occurrence and frequency
4. **Error Report** - Any lexical errors found (if applicable)

### Example Output
```
================================================================================
TOKENS
================================================================================
<KEYWORD, "start", Line: 1, Col: 1>
<KEYWORD, "declare", Line: 2, Col: 5>
<IDENTIFIER, "Count", Line: 2, Col: 13>
<ASSIGNMENT_OP, "=", Line: 2, Col: 19>
<INTEGER_LITERAL, "0", Line: 2, Col: 21>
...
================================================================================

================================================================================
SCANNING STATISTICS
================================================================================
Total tokens (excluding whitespace & comments): 45
Lines processed: 12
Comments removed: 3

Token count by type:
--------------------------------------------------
  ARITHMETIC_OP            : 5
  ASSIGNMENT_OP            : 8
  IDENTIFIER               : 12
  INTEGER_LITERAL          : 7
  KEYWORD                  : 13
  ...
================================================================================

================================================================================
SYMBOL TABLE
================================================================================
Identifier           | Type            | First Occurrence | Frequency
-------------------------------------------------------------------------------------
Count                | undeclared      | Line: 2    Col: 13   | Frequency: 3
Sum                  | undeclared      | Line: 3    Col: 13   | Frequency: 2
...
================================================================================
```

### Testing

Run all test files to verify scanner functionality:

```bash
# Test all valid tokens
java ManualScanner ../tests/test1.lang

# Test complex expressions
java ManualScanner ../tests/test2.lang

# Test strings and escape sequences
java ManualScanner ../tests/test3.lang

# Test error handling
java ManualScanner ../tests/test4.lang

# Test comments
java ManualScanner ../tests/test5.lang
```

### Troubleshooting

**Issue:** `javac: command not found`
- **Solution:** Install JDK and ensure it's in your PATH

**Issue:** `ClassNotFoundException`
- **Solution:** Ensure you're running `java ManualScanner` from the `src` directory

**Issue:** `FileNotFoundException`
- **Solution:** Check that the file path is correct relative to the `src` directory

---

## Team Members

| Name | Roll Number | Section |
|------|-------------|---------|
| [Your Name] | [Your Roll No] | [Section] |
| [Partner Name] | [Partner Roll No] | [Section] |

---

## Project Structure

```
compiler-assignment/
├── src/
│   ├── TokenType.java          # Token type enumeration
│   ├── Token.java               # Token class
│   ├── SymbolTable.java         # Symbol table management
│   ├── ErrorHandler.java        # Error detection and reporting
│   └── ManualScanner.java       # Main scanner implementation
├── docs/
│   ├── Automata_Design.pdf      # NFA/DFA diagrams and tables
│   └── LanguageGrammar.txt      # Formal grammar specification
├── tests/
│   ├── test1.lang               # All valid tokens
│   ├── test2.lang               # Complex expressions
│   ├── test3.lang               # Strings with escapes
│   ├── test4.lang               # Lexical errors
│   ├── test5.lang               # Comment testing
│   └── TestResults.txt          # Expected test outputs
└── README.md                    # This file
```

---

## Additional Notes

### Pattern Matching Priority
The scanner checks patterns in the following order to avoid ambiguity:
1. Multi-line comments
2. Single-line comments
3. Multi-character operators (**, ==, !=, etc.)
4. Keywords
5. Boolean literals
6. Identifiers
7. Floating-point literals
8. Integer literals
9. String/character literals
10. Single-character operators
11. Punctuators
12. Whitespace

### Error Recovery
The scanner continues after encountering errors, reporting all issues rather than stopping at the first error. This provides comprehensive feedback for debugging.

### Longest Match Principle
When multiple patterns match, the scanner always chooses the longest match. For example, `**` is recognized as exponentiation rather than two separate `*` operators.

---

## References

- Aho, A. V., Lam, M. S., Sethi, R., & Ullman, J. D. (2006). *Compilers: Principles, Techniques, and Tools* (2nd ed.). Addison-Wesley. (Dragon Book)
- [Introduction to Compiler Design - GeeksforGeeks](https://www.geeksforgeeks.org/introduction-of-compiler-design/)

---

**Last Updated:** February 11, 2026
