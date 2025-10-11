@echo off
REM Script de lancement du CAD Workshop Demo
REM WaxLibrary3 - W2D Engine Test

echo ╔════════════════════════════════════════════════════════════╗
echo ║                                                            ║
echo ║          CAD WORKSHOP DEMO - W2D ENGINE TEST               ║
echo ║                     WaxLibrary3 v3.2                       ║
echo ║                                                            ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

REM Vérifie si Maven est disponible
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [INFO] Maven détecté, compilation et lancement...
    echo.
    mvn clean compile exec:java -Dexec.mainClass="io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo"
) else (
    echo [WARN] Maven non trouvé, tentative avec javac...
    echo.

    REM Compile avec javac
    echo [INFO] Compilation des sources...
    if not exist target\classes mkdir target\classes

    javac -encoding UTF-8 -d target\classes -sourcepath src\main\java ^
        src\main\java\io\github\warnotte\waxlib3\W2D\PanelGraphique\demo\cadworkshop\*.java ^
        src\main\java\io\github\warnotte\waxlib3\W2D\PanelGraphique\demo\cadworkshop\model\*.java ^
        src\main\java\io\github\warnotte\waxlib3\W2D\PanelGraphique\demo\cadworkshop\view\*.java

    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Erreur de compilation !
        echo.
        echo Ouvrez le projet dans votre IDE (IntelliJ/Eclipse) et lancez:
        echo   io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo
        pause
        exit /b 1
    )

    echo [INFO] Compilation réussie !
    echo.
    echo [INFO] Lancement de la démo...
    echo.

    REM Lance l'application
    java -cp target\classes io.github.warnotte.waxlib3.W2D.PanelGraphique.demo.cadworkshop.CADWorkshopDemo

    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo [ERROR] Erreur au lancement !
        echo.
        echo Vérifiez que toutes les dépendances sont compilées.
        echo Il est recommandé d'utiliser Maven ou votre IDE.
        pause
        exit /b 1
    )
)

echo.
echo [INFO] Démo terminée.
pause
