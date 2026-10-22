# Fase 1: Base, modelos y DAOs (en paralelo)

> **Ramas:** Dev A trabaja en `dev-a`, Dev B en `dev-b`. Cada uno entrega con PR a `develop`. Al cerrar la fase, PR `develop` -> `main`.
> **Antes de empezar:** `git checkout dev-x` y `git pull --rebase origin develop`.

Objetivo: infraestructura lista y toda la capa de datos escrita.

## Orden dentro de la fase

1. A entrega **primero** la infraestructura y los modelos/DAOs de Student, Guardian y Teacher (merge a `main`). B los necesita.
2. Mientras tanto B trabaja en lo que no depende de A.

## Dev A

Infraestructura (entregar primero, PR 1):
- [ ] `config/AppConfig` (Singleton, lee `config.properties` y `LH_DB_PASSWORD`).
- [ ] `config/DatabaseConnection` (Singleton, fábrica de conexiones).
- [ ] `src/main/resources/config.properties` de ejemplo (sin contraseña real).
- [ ] `util/CustomExceptions` (`DataAccessException`, `BusinessException`), `util/LoggerUtility` (`logs/error_log.txt`), `util/InputValidator`.
- [ ] `dao/BaseJdbcDao` (PreparedStatement y try-with-resources centralizados).
- [ ] `view/BaseView` (marcos, tablas, lectura validada, "0 para volver") y `controller/BaseController` (`safely`).

Modelos y DAOs del núcleo (PR 2 y siguientes):
- [ ] Modelos: `Student`, `Guardian`, `GradeLevel`, `Enrollment`, `Subject`, `SubjectEnrollment`, `AcademicRecord`, `Attendance`, `Teacher`, `TeachingAssignment`, `ClassSchedule`, `AverageLine`, `Names`.
- [ ] DAOs (interfaz + `Impl`): `StudentDao`, `GuardianDao`, `GradeLevelDao`, `EnrollmentDao`, `SubjectDao`, `SubjectEnrollmentDao`, `AcademicRecordDao`, `AttendanceDao`, `TeacherDao`, `AssignmentDao`.

## Dev B

Sin depender de A (empezar de inmediato):
- [ ] Modelos: `Club`, `SchoolEvent`, `PaymentConcept`, `Payment`, `Invoice`, `Scholarship`, `ResourceCategory`, `Resource`, `Loan`.
- [ ] Datos de prueba (DML) en `docs/database.sql`: mínimo 30 registros en estudiantes, acudientes, profesores, pagos, clubes y recursos; 60 materias; 300 registros académicos. Coordinar con A el orden de inserción para respetar claves foráneas.

Cuando A haga merge de la infraestructura (hacer `git pull --rebase origin main`):
- [ ] DAOs (interfaz + `Impl`): `ClubDao`, `EventDao`, `PaymentDao`, `InvoiceDao`, `ScholarshipDao`, `ResourceDao`, `LoanDao`, `ReportDao`.

## Criterio de salida

- `mvn clean compile` sin errores en `main`.
- Cada DAO probado con una consulta real contra la base (puede ser un `main` temporal que no se sube).
- Sin SQL concatenado con datos del usuario.
