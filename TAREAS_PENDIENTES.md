# Tareas Pendientes — TFG Gestión de Incidencias

## Corregido (31/03/2026)

### Backend
- [x] Double BCrypt encoding al actualizar usuario — `UsuariosServiceImpl.java`
- [x] Estado inicial de incidencias cambiado de EN_PROGRESO a ABIERTO — `IncidenciasEntity.java`, `IncidenciasServiceImpl.java`
- [x] CascadeType.ALL eliminado de inventario→aulas — `InventarioEntity.java`
- [x] aulaId se asigna correctamente al reportar incidencia — `IncidenciaController.java`
- [x] IncidenciasDTO.fromEntity() ahora mapea aulaId y usuarioId
- [x] Mensaje de error de estados corregido con valores reales del enum
- [x] Usuario "tecnico" duplicado eliminado de DataLoader
- [x] JWT secret movido a application.properties con @Value
- [x] JwtService.java (dead code) eliminado
- [x] Usuarios inactivos rechazados en login — `UserDetailsServiceImpl.java`
- [x] Endpoint /api/incidencias/filtrar protegido (requiere autenticación)
- [x] System.out.println de debug eliminados — `JwtAuthenticationFilter.java`, `JwtTokenProvider.java`, `SecurityConfig.java`
- [x] show-sql desactivado en application.properties

### Frontend
- [x] Logout ahora borra token, username y role de localStorage
- [x] SelectAula usa apiService en vez de URL hardcodeada
- [x] PagInventario implementada (tabla + botones + badges)
- [x] PagNuevoActivo implementada (formulario de creación)
- [x] SideBarUser muestra el nombre real del usuario logueado
- [x] SideBarNav filtra enlaces según rol del usuario
- [x] Rutas /nuevaAveria, /nuevoActivo, /editarActivo dentro del MainLayout
- [x] PagNuevaAveria usa SelectAulas dinámico + redirect tras éxito
- [x] PagPanelControl con estadísticas de activos reales (fetch /inventario)
- [x] Botón "Nuevo Activo" en ControlPanel con navegación
- [x] PagUsuarios — título corregido a "Gestión de Usuarios" + filtros de rol
- [x] PagActivos — título corregido a "Gestión de Activos" + filtros reales
- [x] Página 403 muestra "403" en vez de "404"
- [x] console.log de debug eliminados de PagEditarActivo
- [x] Fallback de apiConfig.ts corregido al puerto 5555
- [x] ESLint: de 99 problemas a 0 errores / 30 warnings
- [x] TypeScript compila sin errores
- [x] LoginForm guarda username en localStorage

## Pendiente (mejoras opcionales para futuras iteraciones)

### Backend
- [ ] Añadir @RestControllerAdvice para manejo global de errores
- [ ] Configurar límite de tamaño de uploads (spring.servlet.multipart.max-file-size)
- [ ] Añadir volumen Docker para persistir uploads
- [ ] Endpoint de eliminación de comentarios
- [ ] Proteger /api/auth/registro (solo ADMIN debería crear usuarios)
- [ ] Considerar cambiar ddl-auto=update a validate para producción

### Frontend
- [ ] Reemplazar alert() por MUI Snackbar para feedback al usuario
- [ ] Añadir AuthContext en vez de leer localStorage directamente
- [ ] Dockerfile de producción (multi-stage build con nginx)
- [ ] Crear .env.production y .env.example
- [ ] Tests unitarios y de integración
