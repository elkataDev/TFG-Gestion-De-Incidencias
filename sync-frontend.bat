@echo off
REM Script de sincronización con Frontend para Windows CMD
REM Uso: sync-frontend.bat

setlocal enabledelayedexpansion

echo ========================================
echo    Sincronizacion con Frontend
echo ========================================
echo.

REM Obtener rama actual
for /f "tokens=*" %%i in ('git branch --show-current') do set CURRENT_BRANCH=%%i

REM Verificar cambios sin commitear
git diff-index --quiet HEAD --
if errorlevel 1 (
    echo [ERROR] Tienes cambios sin commitear
    echo.
    echo Haz commit o stash de tus cambios primero:
    echo    git add .
    echo    git commit -m "tu mensaje"
    echo    o
    echo    git stash
    exit /b 1
)

echo [INFO] Rama actual: %CURRENT_BRANCH%
echo.

REM Guardar hash actual
for /f "tokens=*" %%i in ('git rev-parse HEAD') do set CURRENT_HASH=%%i
echo [INFO] Hash actual guardado: %CURRENT_HASH%
echo.

REM Fetch de Frontend
echo [INFO] Obteniendo cambios de origin/Frontend...
git fetch origin Frontend
if errorlevel 1 (
    echo [ERROR] Fallo al hacer fetch
    exit /b 1
)

REM Intentar merge
echo [INFO] Mergeando origin/Frontend en %CURRENT_BRANCH%...
git merge origin/Frontend --no-ff -m "chore: sync with Frontend branch"
if errorlevel 1 (
    echo.
    echo ========================================
    echo    CONFLICTOS DETECTADOS
    echo ========================================
    echo.
    echo Pasos a seguir:
    echo 1. Resuelve los conflictos en los archivos marcados
    echo 2. git add ^<archivos-resueltos^>
    echo 3. git commit
    echo 4. Vuelve a ejecutar este script
    echo.
    echo O para cancelar el merge:
    echo    git merge --abort
    echo.
    echo Para volver al estado anterior:
    echo    git reset --hard %CURRENT_HASH%
    exit /b 1
)

echo [SUCCESS] Merge exitoso
echo.

REM Push a rama actual
echo [INFO] Pusheando a origin/%CURRENT_BRANCH%...
git push origin %CURRENT_BRANCH%
if errorlevel 1 (
    echo [ERROR] Fallo al pushear a %CURRENT_BRANCH%
    exit /b 1
)

REM Push a Frontend
echo [INFO] Pusheando a origin/Frontend...
git push origin HEAD:Frontend
if errorlevel 1 (
    echo [ERROR] Fallo al pushear a Frontend
    exit /b 1
)

echo.
echo ========================================
echo    Sincronizacion completa!
echo ========================================
