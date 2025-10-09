@echo off
echo Creating new distribution with JavaFX 21.0.8...

:: Создаем папку
if exist "HiddenObjectsGame-21.0.8" rmdir /s /q "HiddenObjectsGame-21.0.8"
mkdir "HiddenObjectsGame-21.0.8"
mkdir "HiddenObjectsGame-21.0.8\javafx-libs"

echo Copying JavaFX 21.0.8...
:: ЗАМЕНИ ЭТОТ ПУТЬ на свой реальный путь к JavaFX 21.0.8
set JAVAFX_SDK_PATH=C:\Users\moska\IdeaLibs\javafx-sdk-21.0.8\lib
xcopy "%JAVAFX_SDK_PATH%\*" "HiddenObjectsGame-21.0.8\javafx-libs\" /Y

echo Copying game...
copy "target\HiddenObjects-1.0-SNAPSHOT.jar" "HiddenObjectsGame-21.0.8\"
xcopy "src\main\resources" "HiddenObjectsGame-21.0.8\" /E /I /Y

echo Creating launcher...
(
echo @echo off
echo title Hidden Objects Game
echo.
echo echo Starting game with JavaFX 21.0.8...
echo java --module-path "javafx-libs" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics,javafx.base -jar "HiddenObjects-1.0-SNAPSHOT.jar"
echo pause
) > "HiddenObjectsGame-21.0.8\PlayGame.bat"

echo.
echo NEW DISTRIBUTION CREATED!
echo Use folder: HiddenObjectsGame-21.0.8
pause