# 🔄 Scripts de Sincronización con Frontend

Scripts para mantener sincronizadas las ramas personales con la rama `Frontend` compartida.

## 📋 Archivos

- **`sync-frontend.sh`** - Para Linux/Mac (Bash)
- **`sync-frontend.ps1`** - Para Windows (PowerShell)
- **`sync-frontend.bat`** - Para Windows (CMD/Command Prompt)

## 🐧 Linux / Mac (Antonio)

### Opción 1: Usando el script directamente

```bash
cd /ruta/al/proyecto/TFG-Gestion-De-Incidencias
./sync-frontend.sh
```

### Opción 2: Usando el alias de Git (recomendado)

```bash
git syncfrontend
```

### Configurar el alias (solo la primera vez)

```bash
cd TFG-Gestion-De-Incidencias
git config alias.syncfrontend '!f() {
  CURRENT_BRANCH=$(git branch --show-current);
  echo "📍 Rama actual: $CURRENT_BRANCH";
  git fetch origin Frontend;
  git merge origin/Frontend --no-ff -m "chore: sync with Frontend branch";
  if [ $? -eq 0 ]; then
    git push origin $CURRENT_BRANCH;
    git push origin HEAD:Frontend;
    echo "🎉 Sincronización completa!";
  fi
}; f'
```

## 🪟 Windows (Alejandro)

### Opción 1: PowerShell (Recomendado)

```powershell
cd C:\ruta\al\proyecto\TFG-Gestion-De-Incidencias
.\sync-frontend.ps1
```

**Nota:** Si obtienes error de política de ejecución, ejecuta primero:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Opción 2: Command Prompt (CMD)

```cmd
cd C:\ruta\al\proyecto\TFG-Gestion-De-Incidencias
sync-frontend.bat
```

### Opción 3: Alias de Git en Windows

Abre PowerShell o Git Bash y ejecuta:

```bash
cd TFG-Gestion-De-Incidencias
git config alias.syncfrontend "!powershell -ExecutionPolicy Bypass -File sync-frontend.ps1"
```

Luego podrás usar:

```bash
git syncfrontend
```

## 🎯 ¿Qué hace el script?

1. **Verifica** que no tengas cambios sin commitear
2. **Guarda** el hash del commit actual (por seguridad)
3. **Fetch** de los cambios de `origin/Frontend`
4. **Merge** de `origin/Frontend` en tu rama actual (Antonio/Alejandro)
5. **Push** de tu rama actualizada a origin
6. **Push** de los cambios a `origin/Frontend`

## ⚠️ Antes de usar el script

**Asegúrate de haber commiteado tus cambios:**

```bash
git add .
git commit -m "descripción de tus cambios"
```

O guárdalos temporalmente con stash:

```bash
git stash
# Después de sincronizar:
git stash pop
```

## 🆘 Si hay conflictos

El script se detendrá y te mostrará instrucciones:

1. Resuelve los conflictos en los archivos marcados
2. `git add <archivos-resueltos>`
3. `git commit`
4. Vuelve a ejecutar el script

Para cancelar el merge:

```bash
git merge --abort
```

## 🔙 Volver al estado anterior

Si algo sale mal, el script te mostrará el hash para volver atrás:

```bash
git reset --hard <hash-mostrado>
```

## 💡 Flujo de trabajo recomendado

### Antonio (Linux):

```bash
# 1. Hacer cambios en rama Antonio
git add .
git commit -m "feat: añadir nuevo componente"

# 2. Sincronizar con Frontend
git syncfrontend

# 3. ¡Listo! Tus cambios están en Frontend y Antonio
```

### Alejandro (Windows):

```powershell
# 1. Hacer cambios en rama Alejandro
git add .
git commit -m "feat: añadir nueva funcionalidad"

# 2. Sincronizar con Frontend
.\sync-frontend.ps1
# o
git syncfrontend

# 3. ¡Listo! Tus cambios están en Frontend y Alejandro
```

## 🚫 NO hacer

❌ **NO** usar comandos manuales como:

```bash
git merge origin/Frontend && git push origin HEAD:Frontend
```

❌ **NO** hacer push directamente a Frontend sin pasar por tu rama:

```bash
git push origin Frontend  # ❌ MAL
```

✅ **SÍ** usar siempre el script o el alias `git syncfrontend`

## 📞 Soporte

Si tienes problemas:

1. Copia el mensaje de error
2. Ejecuta `git status` y copia el resultado
3. Comparte en el grupo

---

**Última actualización:** 13 de octubre de 2025
