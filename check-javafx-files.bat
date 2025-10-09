@echo off
echo Checking JavaFX files in javafx-libs folder...
echo.

cd /d "HiddenObjectsGame-21.0.8"

echo Files in javafx-libs:
dir javafx-libs
echo.

echo Checking for essential modules:
if exist "javafx-libs\javafx.base.jar" (
    echo ✓ javafx.base.jar found
) else (
    echo ✗ javafx.base.jar MISSING!
)

if exist "javafx-libs\javafx.graphics.jar" (
    echo ✓ javafx.graphics.jar found
) else (
    echo ✗ javafx.graphics.jar MISSING!
)

if exist "javafx-libs\javafx.controls.jar" (
    echo ✓ javafx.controls.jar found
) else (
    echo ✗ javafx.controls.jar MISSING!
)

echo.
pause