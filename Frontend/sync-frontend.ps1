# Script de sincronización con Frontend para Windows PowerShell
# Uso: .\sync-frontend.ps1

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

# Verificar cambios sin commitear
$status = git status --porcelain
if ($status) {
    Write-ColorOutput Red "❌ Error: Tienes cambios sin commitear"
    Write-ColorOutput Yellow "`nHaz commit o stash de tus cambios primero:"
    Write-Output "   git add ."
    Write-Output "   git commit -m 'tu mensaje'"
    Write-Output "   o"
    Write-Output "   git stash"
    exit 1
}

Write-ColorOutput Blue "📍 Rama actual: $currentBranch"
Write-Output ""

# Guardar hash actual
$currentHash = git rev-parse HEAD
Write-ColorOutput Blue "💾 Hash actual guardado: $currentHash"
Write-Output ""

# Fetch de Frontend
Write-ColorOutput Yellow "📥 Obteniendo cambios de origin/Frontend..."
git fetch origin Frontend

# Intentar merge
Write-ColorOutput Yellow "🔄 Mergeando origin/Frontend en $currentBranch..."
try {
    git merge origin/Frontend --no-ff -m "chore: sync with Frontend branch"
    
    Write-ColorOutput Green "✅ Merge exitoso"
    Write-Output ""
    
    # Push a rama actual
    Write-ColorOutput Yellow "📤 Pusheando a origin/$currentBranch..."
    git push origin $currentBranch
    
    # Push a Frontend
    Write-ColorOutput Yellow "📤 Pusheando a origin/Frontend..."
    git push origin HEAD:Frontend
    
    Write-Output ""
    Write-Header "🎉 Sincronización completa!"
}
catch {
    Write-Output ""
    Write-ColorOutput Red "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    Write-ColorOutput Red "   ⚠️  CONFLICTOS DETECTADOS"
    Write-ColorOutput Red "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    Write-Output ""
    Write-ColorOutput Yellow "Pasos a seguir:"
    Write-Output "1. Resuelve los conflictos en los archivos marcados"
    Write-Output "2. git add <archivos-resueltos>"
    Write-Output "3. git commit"
    Write-Output "4. Vuelve a ejecutar este script"
    Write-Output ""
    Write-ColorOutput Yellow "O para cancelar el merge:"
    Write-Output "   git merge --abort"
    Write-Output ""
    Write-ColorOutput Yellow "Para volver al estado anterior:"
    Write-Output "   git reset --hard $currentHash"
    exit 1
}
