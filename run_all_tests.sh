#!/bin/bash

# run_all_tests.sh
# Batch script to run all test files and save outputs
# Usage: ./run_all_tests.sh

echo "================================================================================"
echo "RUNNING ALL TESTS - CS4031 Lexical Analyzer"
echo "================================================================================"
echo ""

# Check if we're in the src directory
if [ ! -f "ManualScanner.java" ]; then
    echo "ERROR: Please run this script from the src directory"
    echo "Usage: cd src && ../run_all_tests.sh"
    exit 1
fi

# Check if compiled
if [ ! -f "ManualScanner.class" ]; then
    echo "Class files not found. Compiling..."
    javac *.java
    if [ $? -ne 0 ]; then
        echo "ERROR: Compilation failed!"
        exit 1
    fi
    echo "✓ Compilation successful"
    echo ""
fi

# Create output directory
OUTPUT_DIR="../test_outputs"
mkdir -p "$OUTPUT_DIR"

# Array of test files
TESTS=("test1.lang" "test2.lang" "test3.lang" "test4.lang" "test5.lang")
DESCRIPTIONS=(
    "All Valid Tokens"
    "Complex Expressions"
    "Strings and Escapes"
    "Lexical Errors"
    "Comments Testing"
)

# Run each test
for i in "${!TESTS[@]}"; do
    TEST="${TESTS[$i]}"
    DESC="${DESCRIPTIONS[$i]}"
    OUTPUT_FILE="$OUTPUT_DIR/${TEST%.lang}_output.txt"
    
    echo "================================================================================
"
    echo "Test $((i+1))/5: $DESC"
    echo "File: $TEST"
    echo "--------------------------------------------------------------------------------"
    
    if [ ! -f "../tests/$TEST" ]; then
        echo "❌ ERROR: Test file not found: ../tests/$TEST"
        echo ""
        continue
    fi
    
    # Run the scanner and capture output
    java ManualScanner "../tests/$TEST" > "$OUTPUT_FILE" 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✓ Test completed successfully"
        echo "  Output saved to: $OUTPUT_FILE"
        
        # Show summary statistics
        if grep -q "Total tokens" "$OUTPUT_FILE"; then
            echo ""
            echo "  Quick Stats:"
            grep "Total tokens" "$OUTPUT_FILE" | sed 's/^/    /'
            grep "Lines processed" "$OUTPUT_FILE" | sed 's/^/    /'
            
            if grep -q "Total errors found" "$OUTPUT_FILE"; then
                grep "Total errors found" "$OUTPUT_FILE" | sed 's/^/    /'
            else
                echo "    ✓ No lexical errors found"
            fi
        fi
    else
        echo "❌ Test failed with errors"
        echo "  Check output file: $OUTPUT_FILE"
    fi
    
    echo ""
done

echo "================================================================================"
echo "ALL TESTS COMPLETED"
echo "================================================================================"
echo ""
echo "Output files saved in: $OUTPUT_DIR/"
echo ""
echo "Summary:"
echo "--------"

# Count successes and failures
SUCCESS_COUNT=0
ERROR_COUNT=0

for i in "${!TESTS[@]}"; do
    TEST="${TESTS[$i]}"
    OUTPUT_FILE="$OUTPUT_DIR/${TEST%.lang}_output.txt"
    
    if [ -f "$OUTPUT_FILE" ]; then
        if grep -q "SCANNING STATISTICS" "$OUTPUT_FILE"; then
            SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
            echo "  ✓ ${TESTS[$i]}: SUCCESS"
        else
            ERROR_COUNT=$((ERROR_COUNT + 1))
            echo "  ❌ ${TESTS[$i]}: FAILED"
        fi
    else
        ERROR_COUNT=$((ERROR_COUNT + 1))
        echo "  ❌ ${TESTS[$i]}: NOT RUN"
    fi
done

echo ""
echo "Results: $SUCCESS_COUNT passed, $ERROR_COUNT failed out of ${#TESTS[@]} tests"
echo ""

# Create summary report
SUMMARY_FILE="$OUTPUT_DIR/test_summary.txt"
echo "Creating summary report..."
cat > "$SUMMARY_FILE" << EOF
================================================================================
TEST SUMMARY REPORT
================================================================================
Generated: $(date)

Total Tests Run: ${#TESTS[@]}
Passed: $SUCCESS_COUNT
Failed: $ERROR_COUNT

================================================================================
INDIVIDUAL TEST RESULTS
================================================================================

EOF

for i in "${!TESTS[@]}"; do
    TEST="${TESTS[$i]}"
    DESC="${DESCRIPTIONS[$i]}"
    OUTPUT_FILE="$OUTPUT_DIR/${TEST%.lang}_output.txt"
    
    echo "" >> "$SUMMARY_FILE"
    echo "Test $((i+1)): $DESC ($TEST)" >> "$SUMMARY_FILE"
    echo "----------------------------------------" >> "$SUMMARY_FILE"
    
    if [ -f "$OUTPUT_FILE" ]; then
        if grep -q "Total tokens" "$OUTPUT_FILE"; then
            grep "Total tokens" "$OUTPUT_FILE" >> "$SUMMARY_FILE"
            grep "Lines processed" "$OUTPUT_FILE" >> "$SUMMARY_FILE"
            grep "Comments removed" "$OUTPUT_FILE" >> "$SUMMARY_FILE"
            
            if grep -q "Total errors found" "$OUTPUT_FILE"; then
                grep "Total errors found" "$OUTPUT_FILE" >> "$SUMMARY_FILE"
            else
                echo "✓ No lexical errors found" >> "$SUMMARY_FILE"
            fi
        else
            echo "ERROR: Test did not complete successfully" >> "$SUMMARY_FILE"
        fi
    else
        echo "ERROR: Output file not found" >> "$SUMMARY_FILE"
    fi
done

echo "✓ Summary report saved to: $SUMMARY_FILE"
echo ""
echo "To view individual test outputs, check files in: $OUTPUT_DIR/"
echo ""
echo "================================================================================"
