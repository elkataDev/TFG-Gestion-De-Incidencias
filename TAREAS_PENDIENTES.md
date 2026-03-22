# 📋 Tareas Pendientes (Análisis de Estado del Proyecto)

Tras revisar la especificación del `README.md` y comparar con el código fuente actual (Backend y Frontend), se han detectado las siguientes características pendientes de implementación para completar los requisitos del sistema:

## ⚙️ Backend (Spring Boot)

1. **Gestión de Archivos Adjuntos**
   - *Problema:* `IncidenciasEntity` no contiene un campo para almacenar referencias a archivos y no existen servicios de Storage.
   - *Acción:* Modificar entidades para soportar adjuntos, configurar `MultipartFile` en los controladores, y crear un servicio para guardar los ficheros localmente o en la nube.

2. **Comentarios e Historial de Incidencias**
   - *Problema:* No hay soporte para añadir comentarios a los tickets ni para registrar automáticamente un log de cambios de estado.
   - *Acción:* 
     - Crear `ComentarioEntity` y `HistorialEstadoEntity` (o similar).
     - Desarrollar sus respectivos repositorios, servicios y endpoints REST.

## 💻 Frontend (React)

1. **Subida de Archivos en Formularios**
   - *Problema:* La vista/componente para crear incidencias (`PagNuevaAveria`) necesita soportar la selección y subida de ficheros adjuntos.
   - *Acción:* Actualizar el formulario y la petición a la API.

2. **Vista de Detalle de Incidencia Extendida**
   - *Problema:* Falta la visualización de los hilos de comentarios y el historial de transiciones del ticket en la UI.
   - *Acción:* Desarrollar una vista detallada (o modal ampliado) para cada avería donde los usuarios puedan interactuar con los comentarios.

3. **Dashboards Personalizados por Rol**
   - *Problema:* El diseño requiere vistas diferenciadas (Panel general vs Panel de Solicitante con sus propias métricas).
   - *Acción:* Añadir lógica condicional en `PagPanelControl` o crear componentes de Dashboard específicos, mostrando gráficos/métricas según `role` (ADMIN/TECNICO vs SOLICITANTE).

## 🛠 Entorno y Buenas Prácticas
- **Engram / Memoria del Proyecto:** Guardar decisiones arquitectónicas respecto a cómo se implementarán los adjuntos y comentarios usando el protocolo de memoria establecido.
