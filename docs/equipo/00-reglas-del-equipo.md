# Reglas del equipo

Proyecto: ERP Colegio Internacional "Little Horizons" (Java 17, Maven, MySQL 8, consola, MVC).
Equipo: **Dev A** (núcleo académico e integración) y **Dev B** (servicios, negocio y utilidades).

## Documentos

| Documento | Contenido |
|---|---|
| [06-plan-de-trabajo.md](06-plan-de-trabajo.md) | **Orden exacto de ejecución**, paso a paso, con quién espera a quién |
| [01-fase-0-preparacion.md](01-fase-0-preparacion.md) | Repo, ramas, requisitos, contratos y base de datos |
| [02-fase-1-base-y-modelos.md](02-fase-1-base-y-modelos.md) | Infraestructura, modelos y DAOs |
| [03-fase-2-servicios-y-vistas.md](03-fase-2-servicios-y-vistas.md) | Reglas de negocio, controladores y vistas por módulo |
| [04-fase-3-factura-e-integracion.md](04-fase-3-factura-e-integracion.md) | Factura PDF, descuentos y cableado en `Main` |
| [05-fase-4-reportes-y-cierre.md](05-fase-4-reportes-y-cierre.md) | Reportes, pruebas cruzadas, README y entrega |

## Propiedad de archivos

Cada archivo tiene un solo dueño. Nadie edita un archivo ajeno: se abre un issue o se pide un PR al dueño.

| Dueño | Carpetas y archivos |
|---|---|
| **Dev A** | `pom.xml`, `config/` y `src/main/resources/config.properties`, `dao/BaseJdbcDao`, `view/BaseView`, `controller/BaseController`, `Main`, `MainController`, `MainMenuView`, `docs/sql/01-schema.sql`, y todo lo de Estudiantes, Académico y Profesores |
| **Dev B** | `util/` completo (`CustomExceptions`, `LoggerUtility`, `InputValidator`, `util/pdf/PdfGenerator`), `.gitignore`, `docs/sql/02-extras-b.sql` (incluye `becas`), `docs/sql/03-data.sql`, `README.md` (ensamblaje), y todo lo de Clubes, Eventos, Pagos, Facturas, Becas, Recursos, Préstamos y Reportes |
| **Compartido** | `docs/equipo/` (cambios por PR revisado por el otro) |

Entrega final: `docs/database.sql` unificado (concatenación de `01-schema.sql`, `02-extras-b.sql` y `03-data.sql`). Lo genera Dev A en la Fase 4.

### Tablas de A que B solo lee

`ReportDao` (B) y los servicios de B consultan, en solo lectura: `estudiantes`, `acudientes`, `estudiante_acudiente`, `profesores`, `grados`, `matriculas`, `materias`, `registros_academicos`. B nunca escribe en tablas de A. A nunca escribe en `pagos`, `facturas`, `becas`, `clubes`, `membresias_club`, `eventos`, `participantes_evento`, `recursos` ni en las tablas de préstamos.

## Ramas y flujo de git

| Rama | Para qué | Quién escribe |
|---|---|---|
| `main` | Versiones estables y entregables. Siempre compila. | Nadie directo. Solo recibe PR desde `develop` al cerrar cada fase |
| `develop` | Integración: aquí se juntan el trabajo de A y de B | Nadie directo. Solo recibe PR desde `dev-a` y `dev-b` |
| `dev-a` | Rama de trabajo de **Dev A** | Solo Dev A |
| `dev-b` | Rama de trabajo de **Dev B** | Solo Dev B |

- Cada dev trabaja solo en su rama. Si quiere aislar una tarea grande, crea `feat/<modulo>-<tarea>` desde su rama y la mezcla de vuelta en ella.
- Empezar el día: `git checkout dev-x`, luego `git pull --rebase origin develop` (trae lo del otro).
- Entregar: `git push origin dev-x` y abrir PR `dev-x` -> `develop`.
- Antes del PR: `mvn clean compile` sin errores.
- El otro dev revisa y aprueba cada PR a `develop`. Merge con "squash". Revisión en menos de 24 horas para no bloquear.
- Al cerrar una fase, PR `develop` -> `main` (lo abre Dev A, lo aprueba Dev B).
- Nadie hace `push --force` sobre `main` ni `develop`.
- Commits en español, formato `tipo(modulo): descripcion` (ej. `feat(club): valida cupo maximo`). Sin líneas de atribución de herramientas.
- Nunca se sube una contraseña. La clave de MySQL va en la variable de entorno `LH_DB_PASSWORD`.

## Convenciones de código

- Paquete base: `com.littlehorizons.erp`.
- Clases e identificadores en inglés, mensajes al usuario en español.
- Modelos: `record` inmutables, sin lógica de persistencia.
- SQL solo en `dao/`, siempre con `PreparedStatement` y `try-with-resources`.
- Reglas de negocio solo en `service/`. La vista solo hace entrada y salida.
- Errores: `DataAccessException` en DAO, `BusinessException` en service. El usuario ve un mensaje claro; el detalle va a `logs/error_log.txt`.
- Capas: controller -> service -> dao, por interfaz y con inyección por constructor.
- Nadie crea utilidades paralelas: si falta un método en `BaseView` o `InputValidator`, se pide al dueño por issue.

## Claves de `config.properties` (las define A en su primer PR)

`db.url`, `db.user`, `db.password` (se sobrescribe con `LH_DB_PASSWORD`), `paths.logFile`, `paths.invoicesDir`, `business.academicYear`, `business.delinquencyConcept`, `business.delinquencyDays`, `business.siblingDiscountPercent`, `business.loanDays`, `business.weeklyAbsenceThreshold`, `school.name`, `school.nit`, `school.address`, `school.phone`, `school.email`, `school.resolution`.

Si B necesita una clave nueva, la pide a A por issue.

## Cambios al esquema de base de datos

El DDL oficial se modifica solo con justificación escrita en el README (sección 6.1). Cambios previstos:
- `horarios_clase` (requisito 1.2), la agrega A en `01-schema.sql`.
- `becas` (requisito 3.1), la agrega B en `02-extras-b.sql`.
- Cualquier otro campo que B necesite (cupo, recibo, CUFE) lo redacta B en `02-extras-b.sql`.

## Definición de "terminado" para cualquier tarea

1. Compila (`mvn clean compile`).
2. Entradas validadas con mensaje claro y reintento.
3. Mensaje de éxito o error en cada operación.
4. Se puede volver al menú anterior desde cualquier punto.
5. Probado a mano contra la base `erp_little_horizons`.
6. PR revisado por el otro dev.
