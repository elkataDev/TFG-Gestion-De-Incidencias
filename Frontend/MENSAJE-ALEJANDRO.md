# 📧 Mensaje para Alejandro

Hola Alejandro,

He creado scripts de sincronización para evitar que se pierdan cambios cuando hacemos merge. **Ya no uses el comando manual**, usa estos scripts.

## 🎯 Para ti (Windows):

### 1️⃣ Haz pull para obtener los scripts

```bash
git pull origin Frontend
```

### 2️⃣ Ve a la carpeta Frontend

```powershell
cd Frontend
```

### 3️⃣ Opción A: Usar PowerShell (RECOMENDADO)

```powershell
.\sync-frontend.ps1
```

Si te da error de permisos, ejecuta primero:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### 3️⃣ Opción B: Usar CMD

```cmd
sync-frontend.bat
```

### 3️⃣ Opción C: Crear alias de Git (más cómodo)

Desde la carpeta Frontend:

```bash
git config alias.syncfrontend "!powershell -ExecutionPolicy Bypass -File sync-frontend.ps1"
```

Luego solo ejecuta:

```bash
git syncfrontend
```

## 📝 Flujo de trabajo desde ahora:

```bash
# 1. Haces tus cambios
git add .
git commit -m "tu mensaje"

# 2. Sincronizas (en lugar del comando anterior)
git syncfrontend
# o
.\sync-frontend.ps1

# ¡Listo! ✅
```

## ❌ YA NO USAR:

```bash
git fetch origin && git merge origin/Frontend && git push origin HEAD:Frontend
```

## 📖 Más información:

Lee el archivo `SYNC-INSTRUCTIONS.md` que tiene toda la documentación.

---

**Ventajas:**

- ✅ No se pierden cambios
- ✅ Detecta conflictos antes de pushear
- ✅ Muestra mensajes claros
- ✅ Permite volver atrás si algo falla

Cualquier duda me dices 👍
