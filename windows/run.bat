@echo off
set FX_LIB=javafx-sdk-21.0.7\lib
java --module-path %FX_LIB% --add-modules javafx.controls,javafx.fxml -jar yootnori_fx1.jar
pause