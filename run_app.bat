@echo off
if not exist bin mkdir bin
echo Compiling...
javac --module-path "javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -d bin src/application/*.java src/controller/*.java src/model/*.java src/service/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Running...
java --module-path "javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp bin application.Launcher
pause