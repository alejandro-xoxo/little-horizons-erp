# Reglas del equipo

Proyecto: ERP Colegio Internacional "Little Horizons" (Java 17, Maven, MySQL 8, consola, MVC).
Equipo: **Dev A** (núcleo académico) y **Dev B** (servicios y negocio).

## Documentos por fase

| Fase | Documento | Qué se logra |
|---|---|---|
| 0 | [01-fase-0-preparacion.md](01-fase-0-preparacion.md) | Repo, ramas, contratos, base de datos |
| 1 | [02-fase-1-base-y-modelos.md](02-fase-1-base-y-modelos.md) | Infraestructura, modelos y DAOs |
| 2 | [03-fase-2-servicios-y-vistas.md](03-fase-2-servicios-y-vistas.md) | Reglas de negocio, controladores y vistas por módulo |
| 3 | [04-fase-3-factura-e-integracion.md](04-fase-3-factura-e-integracion.md) | Factura PDF, descuentos y cableado en `Main` |
| 4 | [05-fase-4-reportes-y-cierre.md](05-fase-4-reportes-y-cierre.md) | Reportes, pruebas cruzadas, README y entrega |

## Propiedad de archivos

| Dueño | Carpetas y archivos |
|---|---|
| **Dev A** | `pom.xml`, `config/`, `util/` (excepto `PdfGenerator`), `BaseJdbcDao`, `BaseView`, `BaseController`, `Main`, `MainController`, `MainMenuView`, DDL de `docs/database.sql`, y todo lo de Estudiantes, Académico y Profesores |
| **Dev B** | Todo lo de Clubes, Eventos, Pagos, Facturas, Becas, Recursos, Préstamos y Reportes, `util/PdfGenerator`, DML de `docs/database.sql`, `README.md` |

Regla: nadie edita un archivo ajeno. Si necesita un cambio, abre un issue o pide un PR al dueño.

## Flujo de git

| Rama | Para qué | Quién escribe |
|---|---|---|
| `main` | Versiones estables y entregables. Siempre compila. | Nadie directo. Solo recibe PR desde `develop` al cerrar cada fase |
| `develop` | Integración: aquí se juntan el trabajo de A y de B | Nadie directo. Solo recibe PR desde `dev-a` y `dev-b` |
| `dev-a` | Rama de trabajo de **Dev A** | Solo Dev A |
| `dev-b` | Rama de trabajo de **Dev B** | Solo Dev B |

- Cada dev trabaja solo en su rama (`dev-a` o `dev-b`). Si quiere aislar una tarea grande, crea `feat/<modulo>-<tarea>` desde su rama y la mezcla de vuelta en ella.
- Para empezar el día: `git checkout dev-x`, luego `git pull --rebase origin develop` (trae lo del otro).
- Para entregar: `git push origin dev-x` y abrir PR `dev-x` -> `develop`.
- Antes del PR: `mvn clean compile` sin errores.
- El otro dev revisa y aprueba cada PR a `develop`. Merge con "squash".
- Al cerrar una fase, PR `develop` -> `main` (lo abre Dev A, lo aprueba Dev B).
- Nadie hace `push --force` sobre `main` ni `develop`.
- Commits en español, formato `tipo(modulo): descripcion` (ej. `feat(club): valida cupo maximo`).
- Nunca se sube `config.properties` con contraseña. La clave va en la variable de entorno `LH_DB_PASSWORD`.

## Convenciones de código

- Paquete base: `com.littlehorizons.erp`.
- Clases e identificadores en inglés (como el resto del paquete), mensajes al usuario en español.
- Modelos: `record` inmutables, sin lógica de persistencia.
- SQL solo en `dao/`, siempre con `PreparedStatement` y `try-with-resources`.
- Reglas de negocio solo en `service/`. La vista solo hace entrada y salida.
- Errores: `DataAccessException` en DAO, `BusinessException` en service. El usuario ve un mensaje claro; el detalle va a `logs/error_log.txt`.
- Dependencias entre capas: controller -> service -> dao (por interfaz, inyectada por constructor).

## Contratos entre desarrolladores

B consume de A solo por interfaz: `StudentDao`, `GuardianDao`, `TeacherDao`. A no depende de código de B, salvo el cableado final en `Main`.

## Cambios al esquema de base de datos

El DDL oficial se puede modificar solo con justificación escrita en el README (sección 6.1). Cambios previstos y justificados: tabla `horarios_clase` (requisito 1.2) y tabla `becas` (requisito 3.1).

## Definición de "terminado" para cualquier tarea

1. Compila (`mvn clean compile`).
2. Entradas validadas con mensaje claro y reintento.
3. Mensaje de éxito o error en cada operación.
4. Se puede volver al menú anterior desde cualquier punto.
5. Probado a mano contra la base `erp_little_horizons`.
6. PR revisado por el otro dev.
