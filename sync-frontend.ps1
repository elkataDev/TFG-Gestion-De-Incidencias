# Script de sincronización con Frontend para Windows PowerShell
# Uso: .\sync-frontend.ps1
# Uso con auto-resolución: .\sync-frontend.ps1 -AutoResolve

# Parámetros
param(
    [switch]$AutoResolve = $false
)

# Configurar para detener en errores
$ErrorActionPreference = "Stop"

# Colores
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Write-Header($message) {
    Write-ColorOutput Blue "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    Write-ColorOutput Blue "   $message"
    Write-ColorOutput Blue "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    Write-Output ""
}

Write-Header "Sincronización con Frontend"

# Obtener rama actual
$currentBranch = git branch --show-current

# Verificar si hay un merge en progreso
if (Test-Path ".git/MERGE_HEAD") {
    Write-ColorOutput Red "[X] Error: Ya hay un merge en progreso"
    Write-ColorOutput Yellow "`nOpciones:"
    Write-Output "   1. Resuelve los conflictos y ejecuta: git commit"
    Write-Output "   2. Cancela el merge con: git merge --abort"
    exit 1
}

# Verificar cambios sin commitear
$status = git status --porcelain
if ($status) {
    Write-ColorOutput Red "[X] Error: Tienes cambios sin commitear"
    Write-ColorOutput Yellow "`nHaz commit o stash de tus cambios primero:"
    Write-Output "   git add ."
    Write-Output "   git commit -m 'tu mensaje'"
    Write-Output "   o"
    Write-Output "   git stash"
    exit 1
}

Write-ColorOutput Blue "[>] Rama actual: $currentBranch"
Write-Output ""

# Guardar hash actual
$currentHash = git rev-parse HEAD
Write-ColorOutput Blue "[*] Hash actual guardado: $currentHash"
Write-Output ""

# Fetch de Frontend
Write-ColorOutput Yellow "[>>] Obteniendo cambios de origin/Frontend..."
git fetch origin Frontend

# Intentar merge
Write-ColorOutput Yellow "[~] Mergeando origin/Frontend en $currentBranch..."
git merge origin/Frontend --no-ff -m "chore: sync with Frontend branch" 2>&1 | Out-Null
$mergeExitCode = $LASTEXITCODE

if ($mergeExitCode -ne 0) {
    # Verificar si hay conflictos
    $conflicts = git diff --name-only --diff-filter=U

    if ($conflicts) {
        Write-Output ""
        Write-ColorOutput Red "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        Write-ColorOutput Red "   [!]  CONFLICTOS DETECTADOS"
        Write-ColorOutput Red "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        Write-Output ""
        Write-ColorOutput Yellow "Archivos en conflicto:"
        $conflicts | ForEach-Object { Write-Output "   - $_" }
        Write-Output ""

        if ($AutoResolve) {
            Write-ColorOutput Cyan "[~] Intentando resolver conflictos automáticamente..."

            $resolvedAll = $true
            foreach ($file in $conflicts) {
                Write-ColorOutput Cyan "   Procesando: $file"

                # Intentar resolver con theirs (mantener cambios de Frontend)
                git checkout --theirs $file 2>&1 | Out-Null
                if ($LASTEXITCODE -eq 0) {
                    git add $file
                    Write-ColorOutput Green "   [OK] Resuelto (usando version de Frontend)"
                } else {
                    Write-ColorOutput Red "   [X] No se pudo resolver automáticamente"
                    $resolvedAll = $false
                }
            }

            if ($resolvedAll) {
                Write-Output ""
                Write-ColorOutput Green "[OK] Todos los conflictos resueltos automáticamente"
                Write-ColorOutput Yellow "[~] Completando merge..."
                git commit --no-edit

                Write-ColorOutput Green "[OK] Merge completado"
                Write-Output ""
            } else {
                Write-Output ""
                Write-ColorOutput Yellow "Algunos conflictos requieren resolución manual:"
                Write-Output "1. Abre los archivos en conflicto y resuélvelos"
                Write-Output "2. git add <archivos-resueltos>"
                Write-Output "3. git commit"
                Write-Output "4. Vuelve a ejecutar este script"
                Write-Output ""
                Write-ColorOutput Yellow "O para cancelar el merge:"
                Write-Output "   git merge --abort"
                exit 1
            }
        } else {
            Write-ColorOutput Yellow "Pasos a seguir:"
            Write-Output "1. Resuelve los conflictos en los archivos marcados"
            Write-Output "2. git add <archivos-resueltos>"
            Write-Output "3. git commit"
            Write-Output "4. Vuelve a ejecutar este script"
            Write-Output ""
            Write-ColorOutput Yellow "O para auto-resolver (usar version de Frontend):"
            Write-Output "   .\sync-frontend.ps1 -AutoResolve"
            Write-Output ""
            Write-ColorOutput Yellow "O para cancelar el merge:"
            Write-Output "   git merge --abort"
            Write-Output ""
            Write-ColorOutput Yellow "Para volver al estado anterior:"
            Write-Output "   git reset --hard $currentHash"
            exit 1
        }
    } else {
        Write-ColorOutput Red "[X] Error en el merge (no hay conflictos detectados)"
        exit 1
    }
}

Write-ColorOutput Green "[OK] Merge exitoso"
Write-Output ""

# Push a rama actual
Write-ColorOutput Yellow "[<<] Pusheando a origin/$currentBranch..."
git push origin $currentBranch

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput Red "[X] Error al pushear a origin/$currentBranch"
    exit 1
}

# Push a Frontend
Write-ColorOutput Yellow "[<<] Pusheando a origin/Frontend..."
git push origin HEAD:Frontend

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput Red "[X] Error al pushear a origin/Frontend"
    exit 1
}

Write-Output ""
Write-Header "[!] Sincronizacion completa!"
