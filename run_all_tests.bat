@echo off
REM run_all_tests.bat
REM Batch script to run all test files and save outputs (Windows)
REM Usage: run_all_tests.bat

echo ================================================================================
echo RUNNING ALL TESTS - CS4031 Lexical Analyzer
echo ================================================================================
echo.

REM Check if we're in the src directory
if not exist "ManualScanner.java" (
    echo ERROR: Please run this script from the src directory
    echo Usage: cd src ^&^& ..\run_all_tests.bat
    exit /b 1
)

REM Check if compiled
if not exist "ManualScanner.class" (
    echo Class files not found. Compiling...
    javac *.java
    if errorlevel 1 (
        echo ERROR: Compilation failed!
        exit /b 1
    )
    echo ✓ Compilation successful
    echo.
)

REM Create output directory
set OUTPUT_DIR=..\test_outputs
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM Test counter
set TEST_NUM=0
set SUCCESS_COUNT=0
set ERROR_COUNT=0

REM Test 1
set /a TEST_NUM+=1
echo ================================================================================
echo Test %TEST_NUM%/5: All Valid Tokens
echo File: test1.lang
echo --------------------------------------------------------------------------------
if exist "..\tests\test1.lang" (
    java ManualScanner "..\tests\test1.lang" > "%OUTPUT_DIR%\test1_output.txt" 2>&1
    if errorlevel 1 (
        echo ❌ Test failed with errors
        set /a ERROR_COUNT+=1
    ) else (
        echo ✓ Test completed successfully
        echo   Output saved to: %OUTPUT_DIR%\test1_output.txt
        set /a SUCCESS_COUNT+=1
    )
) else (
    echo ❌ ERROR: Test file not found
    set /a ERROR_COUNT+=1
)
echo.

REM Test 2
set /a TEST_NUM+=1
echo ================================================================================
echo Test %TEST_NUM%/5: Complex Expressions
echo File: test2.lang
echo --------------------------------------------------------------------------------
if exist "..\tests\test2.lang" (
    java ManualScanner "..\tests\test2.lang" > "%OUTPUT_DIR%\test2_output.txt" 2>&1
    if errorlevel 1 (
        echo ❌ Test failed with errors
        set /a ERROR_COUNT+=1
    ) else (
        echo ✓ Test completed successfully
        echo   Output saved to: %OUTPUT_DIR%\test2_output.txt
        set /a SUCCESS_COUNT+=1
    )
) else (
    echo ❌ ERROR: Test file not found
    set /a ERROR_COUNT+=1
)
echo.

REM Test 3
set /a TEST_NUM+=1
echo ================================================================================
echo Test %TEST_NUM%/5: Strings and Escapes
echo File: test3.lang
echo --------------------------------------------------------------------------------
if exist "..\tests\test3.lang" (
    java ManualScanner "..\tests\test3.lang" > "%OUTPUT_DIR%\test3_output.txt" 2>&1
    if errorlevel 1 (
        echo ❌ Test failed with errors
        set /a ERROR_COUNT+=1
    ) else (
        echo ✓ Test completed successfully
        echo   Output saved to: %OUTPUT_DIR%\test3_output.txt
        set /a SUCCESS_COUNT+=1
    )
) else (
    echo ❌ ERROR: Test file not found
    set /a ERROR_COUNT+=1
)
echo.

REM Test 4
set /a TEST_NUM+=1
echo ================================================================================
echo Test %TEST_NUM%/5: Lexical Errors
echo File: test4.lang
echo --------------------------------------------------------------------------------
if exist "..\tests\test4.lang" (
    java ManualScanner "..\tests\test4.lang" > "%OUTPUT_DIR%\test4_output.txt" 2>&1
    if errorlevel 1 (
        echo ❌ Test failed with errors
        set /a ERROR_COUNT+=1
    ) else (
        echo ✓ Test completed successfully
        echo   Output saved to: %OUTPUT_DIR%\test4_output.txt
        echo   Note: This test should report lexical errors - that's expected!
        set /a SUCCESS_COUNT+=1
    )
) else (
    echo ❌ ERROR: Test file not found
    set /a ERROR_COUNT+=1
)
echo.

REM Test 5
set /a TEST_NUM+=1
echo ================================================================================
echo Test %TEST_NUM%/5: Comments Testing
echo File: test5.lang
echo --------------------------------------------------------------------------------
if exist "..\tests\test5.lang" (
    java ManualScanner "..\tests\test5.lang" > "%OUTPUT_DIR%\test5_output.txt" 2>&1
    if errorlevel 1 (
        echo ❌ Test failed with errors
        set /a ERROR_COUNT+=1
    ) else (
        echo ✓ Test completed successfully
        echo   Output saved to: %OUTPUT_DIR%\test5_output.txt
        set /a SUCCESS_COUNT+=1
    )
) else (
    echo ❌ ERROR: Test file not found
    set /a ERROR_COUNT+=1
)
echo.

echo ================================================================================
echo ALL TESTS COMPLETED
echo ================================================================================
echo.
echo Output files saved in: %OUTPUT_DIR%\
echo.
echo Summary:
echo --------
echo Results: %SUCCESS_COUNT% passed, %ERROR_COUNT% failed out of 5 tests
echo.

REM Create summary report
set SUMMARY_FILE=%OUTPUT_DIR%\test_summary.txt
echo Creating summary report...

echo ================================================================================ > "%SUMMARY_FILE%"
echo TEST SUMMARY REPORT >> "%SUMMARY_FILE%"
echo ================================================================================ >> "%SUMMARY_FILE%"
echo Generated: %date% %time% >> "%SUMMARY_FILE%"
echo. >> "%SUMMARY_FILE%"
echo Total Tests Run: 5 >> "%SUMMARY_FILE%"
echo Passed: %SUCCESS_COUNT% >> "%SUMMARY_FILE%"
echo Failed: %ERROR_COUNT% >> "%SUMMARY_FILE%"
echo. >> "%SUMMARY_FILE%"
echo ================================================================================ >> "%SUMMARY_FILE%"
echo INDIVIDUAL TEST RESULTS >> "%SUMMARY_FILE%"
echo ================================================================================ >> "%SUMMARY_FILE%"

REM Add individual results to summary
for %%f in (test1 test2 test3 test4 test5) do (
    echo. >> "%SUMMARY_FILE%"
    echo Test: %%f.lang >> "%SUMMARY_FILE%"
    echo ---------------------------------------- >> "%SUMMARY_FILE%"
    if exist "%OUTPUT_DIR%\%%f_output.txt" (
        findstr /C:"Total tokens" "%OUTPUT_DIR%\%%f_output.txt" >> "%SUMMARY_FILE%" 2>nul
        findstr /C:"Lines processed" "%OUTPUT_DIR%\%%f_output.txt" >> "%SUMMARY_FILE%" 2>nul
        findstr /C:"Comments removed" "%OUTPUT_DIR%\%%f_output.txt" >> "%SUMMARY_FILE%" 2>nul
        findstr /C:"Total errors found" "%OUTPUT_DIR%\%%f_output.txt" >> "%SUMMARY_FILE%" 2>nul
    ) else (
        echo ERROR: Output file not found >> "%SUMMARY_FILE%"
    )
)

echo ✓ Summary report saved to: %SUMMARY_FILE%
echo.
echo To view individual test outputs, check files in: %OUTPUT_DIR%\
echo.
echo ================================================================================

pause
