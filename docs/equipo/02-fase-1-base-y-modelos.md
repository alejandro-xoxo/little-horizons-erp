# Fase 1: Base, modelos y DAOs (en paralelo)

> **Ramas:** Dev A trabaja en `dev-a`, Dev B en `dev-b`. Cada uno entrega con PR a `develop`. Al cerrar la fase, PR `develop` -> `main`.
> **Antes de empezar:** `git checkout dev-x` y `git pull --rebase origin develop`.

Objetivo: infraestructura lista y toda la capa de datos escrita. Orden exacto en [06-plan-de-trabajo.md](06-plan-de-trabajo.md).

## Orden dentro de la fase

1. **B entrega primero `util/`** (`CustomExceptions`, `LoggerUtility`, `InputValidator`) en un PR corto a `develop`. Son pocas clases y A las necesita.
2. **A entrega su PR 1** (config, `DatabaseConnection`, `BaseJdbcDao`, `BaseView`, `BaseController`) en cuanto el PR de B esté en `develop`. Mientras tanto A escribe contra las firmas del contrato.
3. **A entrega su PR 2:** modelos y DAOs de Student, Guardian, Teacher, Enrollment y GradeLevel. B los necesita.
4. B escribe sus DAOs cuando están en `develop` el PR 1 y el PR 2 de A.

## Dev A

PR 1: infraestructura
- [ ] `config/AppConfig` (Singleton, lee `config.properties` y `LH_DB_PASSWORD`).
- [ ] `config/DatabaseConnection` (Singleton, fábrica de conexiones).
- [ ] `src/main/resources/config.properties` con todas las claves listadas en [00-reglas-del-equipo.md](00-reglas-del-equipo.md), sin contraseña real.
- [ ] `dao/BaseJdbcDao` (PreparedStatement y try-with-resources centralizados).
- [ ] `view/BaseView` (marcos, tablas, lectura validada, "0 para volver") y `controller/BaseController` (`safely`). Congelar su API en este PR.

PR 2: núcleo
- [ ] Modelos y DAOs (interfaz + `Impl`) de `Student`, `Guardian`, `Teacher`, `Enrollment`, `GradeLevel`, con los métodos del contrato.

PR 3 y siguientes
- [ ] Modelos: `Subject`, `SubjectEnrollment`, `AcademicRecord`, `Attendance`, `TeachingAssignment`, `ClassSchedule`, `AverageLine`, `Names`.
- [ ] DAOs: `SubjectDao`, `SubjectEnrollmentDao`, `AcademicRecordDao`, `AttendanceDao`, `AssignmentDao`.

## Dev B

PR 1: utilidades (primero, lo antes posible)
- [ ] `util/CustomExceptions`, `util/LoggerUtility` (escribe en `logs/error_log.txt` con fecha, contexto y stacktrace), `util/InputValidator`, con las firmas del contrato.

Sin depender de A:
- [ ] Modelos: `Club`, `SchoolEvent`, `PaymentConcept`, `Payment`, `Invoice`, `Scholarship`, `ResourceCategory`, `Resource`, `Loan`.
- [ ] `docs/sql/03-data.sql` (datos de prueba) con todas las tablas: mínimo 30 registros en estudiantes, acudientes, profesores, pagos, clubes y recursos; 60 materias; 300 registros académicos. Respetar el orden de claves foráneas y documentarlo en un comentario al inicio del archivo.

Cuando estén en `develop` el PR 1 y el PR 2 de A (hacer `git pull --rebase origin develop`):
- [ ] DAOs (interfaz + `Impl`): `ClubDao`, `EventDao`, `PaymentDao`, `InvoiceDao`, `ScholarshipDao`, `ResourceDao`, `LoanDao`, `ReportDao`.
- [ ] Mientras no estén los DAOs de A, B puede escribir los DAOs que solo usan IDs (no necesitan el modelo `Student`).

## Criterio de salida

- `mvn clean compile` sin errores en `develop`.
- Cada DAO probado con una consulta real contra la base (puede ser un `main` temporal que no se sube).
- Sin SQL concatenado con datos del usuario.
- `03-data.sql` carga completo sobre `01-schema.sql` + `02-extras-b.sql` sin errores.
