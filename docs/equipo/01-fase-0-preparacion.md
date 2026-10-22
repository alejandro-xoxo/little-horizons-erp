# Fase 0: Preparación (juntos, ~medio día)

Objetivo: que ambos puedan trabajar en paralelo sin bloquearse. Orden detallado en [06-plan-de-trabajo.md](06-plan-de-trabajo.md).

## Ramas y qué debe tener cada dev

| | **Dev A** | **Dev B** |
|---|---|---|
| **Rama de trabajo** | `dev-a` | `dev-b` |
| **Entrega a** | PR `dev-a` -> `develop` | PR `dev-b` -> `develop` |
| **Primer comando** | `git clone https://github.com/alejandro-xoxo/little-horizons-erp.git` y `git checkout dev-a` | `git clone https://github.com/alejandro-xoxo/little-horizons-erp.git` y `git checkout dev-b` |

Ramas de apoyo (nadie escribe directo): `develop` (integración) y `main` (estable). Detalle en [00-reglas-del-equipo.md](00-reglas-del-equipo.md).

Cada dev debe tener antes de empezar:
- [ ] Cuenta de GitHub con acceso de escritura al repo (colaborador).
- [ ] Git con `user.name` y `user.email` configurados.
- [ ] JDK 17 o superior (`java -version`).
- [ ] Maven 3.9+ (`mvn -v`) o el Maven de NetBeans.
- [ ] MySQL Server 8.0.16 o superior (`mysql --version`) y un cliente (DBeaver o Workbench).
- [ ] IDE: Apache NetBeans, IntelliJ o VS Code con soporte Maven.
- [ ] Terminal UTF-8 (Windows: `chcp 65001`).
- [ ] Variable de entorno `LH_DB_PASSWORD` con su contraseña local de MySQL.
- [ ] Haber leído las reglas del equipo y el enunciado del proyecto.

## Tareas conjuntas

- [ ] Cada dev clona el repo y cambia a su rama.
- [ ] Confirmar que `main`, `develop`, `dev-a` y `dev-b` existen en GitHub.
- [ ] Revisar este contrato y las reglas de propiedad de archivos.

## Contrato A -> B (interfaces de A que B consume)

| Interfaz (dueño A) | Métodos mínimos |
|---|---|
| `StudentDao` | `findById(int)`, `findAllActive()`, `findByDocument(String)`, `findSiblings(int idStudent)` |
| `GuardianDao` | `findById(int)`, `findByStudent(int)`, `findPrincipalByStudent(int)`, `countStudentsByGuardian(int)` |
| `TeacherDao` | `findById(int)`, `findAllActive()` |
| `EnrollmentDao` | `findByStudentAndYear(int idStudent, int year)` |
| `GradeLevelDao` | `findById(int)`, `findAll()` |

Modelos (records) de A que B usa: `Student`, `Guardian`, `Teacher`, `Enrollment`, `GradeLevel`.

## Contrato B -> A (clases de B que A consume)

| Clase (dueño B) | API mínima |
|---|---|
| `util/CustomExceptions` | `DataAccessException(String, Throwable)`, `BusinessException(String)` (ambas `RuntimeException`) |
| `util/LoggerUtility` | `static void logError(String context, Throwable t)` |
| `util/InputValidator` | `isValidDocument(String)`, `isValidEmail(String)`, `isValidPhone(String)`, `isValidGrade(double)` (0.0 a 5.0 con un decimal) |

Estas firmas son definitivas. Quien necesite algo más lo pide por issue; no se cambian firmas sin avisar.

## Dev A

- [ ] Publicar `docs/sql/01-schema.sql`: DDL oficial del anexo más la tabla `horarios_clase`, con justificación. Ejecutarlo en MySQL (base `erp_little_horizons`).
- [ ] Avisar a B cuando `01-schema.sql` esté en `develop` (B lo necesita para su DML y sus DAOs).
- [ ] Confirmar que `pom.xml` ya declara `mysql-connector-j`, `pdfbox` y `zxing` (están desde el esqueleto).

## Dev B

- [ ] Revisar `01-schema.sql` y redactar `docs/sql/02-extras-b.sql` con la tabla `becas` y cualquier campo faltante para sus módulos (cupo de clubes, número de recibo, CUFE), con justificación.
- [ ] Verificar que las dependencias bajan: `mvn clean compile`.
- [ ] Crear el `.gitignore` definitivo (ya existe uno base; B lo revisa y lo ajusta).

## Criterio de salida

- Ambos compilan el esqueleto con `mvn clean compile`.
- La base `erp_little_horizons` existe en las dos máquinas con `01-schema.sql` y `02-extras-b.sql`.
- Contratos A -> B y B -> A acordados por escrito (este documento).
