# Plan de trabajo de Dev A (rama `dev-a`)

Orden exacto de ejecución. Cada paso termina con algo que se compila y se prueba. Marca las casillas a medida que avanzas. Detalle de requisitos en [02-area-a.md](02-area-a.md). Patrón de código en [01-nucleo-compartido.md](01-nucleo-compartido.md).

Todas las clases van en `src/main/java/com/littlehorizons/erp/` dentro de `model/`, `dao/`, `service/`, `controller/` y `view/`.

## Paso 0: Preparación (30 min)

- [ ] Instalar JDK 17+, Maven 3.9+, MySQL 8.0.16+.
- [ ] Definir `LH_DB_PASSWORD`.
- [ ] `git checkout dev-a` y `git pull --rebase origin develop`.
- [ ] Cargar `docs/sql/01-schema.sql` y `docs/sql/02-data-base.sql`.
- [ ] `mvn clean compile` y `mvn exec:java`: sale el menú principal.

Listo cuando: el menú arranca y `SELECT COUNT(*) FROM estudiantes` da 48.

## Paso 1: Modelos y DAOs de personas (módulo 1, base)

Tablas: `estudiantes`, `acudientes`, `estudiante_acudiente`, `grados`, `matriculas`.

- [ ] Modelos (records): `Student`, `Guardian`, `GradeLevel`, `Enrollment`.
- [ ] `GradeLevelDao`/`Impl`: `findAll()`, `findById(int)`.
- [ ] `StudentDao`/`Impl`: `insert`, `update`, `findById`, `findByDocument`, `search(String texto)` por nombre o documento, `findByGrade(idGrado, año)`, `updateStatus(id, estado)`.
- [ ] `GuardianDao`/`Impl`: `insert`, `update`, `findById`, `findByDocument`, `findByStudent(idEstudiante)`, `link(idEstudiante, idAcudiente, esPrincipal)`, `unlink`, `countStudents(idAcudiente)`.
- [ ] `EnrollmentDao`/`Impl`: `insert`, `findByStudentAndYear`, `findByStudent` (historial), `updateGrade`, `updateStatus`.

Listo cuando: un `main` temporal (no se sube) imprime el estudiante 1 con su acudiente y su matrícula 2026.

## Paso 2: Servicio, vista y controlador de Estudiantes (módulo 1)

- [ ] `StudentService` con reglas: documento único; fecha de nacimiento pasada; edad coherente con el grado (±3 años); todo estudiante con al menos un acudiente; un solo acudiente principal; no matricular dos veces el mismo año; cambio de estado Activo, Graduado, Retirado, Suspendido (retirado: cierra la matrícula con fecha).
- [ ] `StudentView` extiende `BaseView`: formularios con `readRequired`, `readDate`, `readChoice`; ficha completa; tabla de listado.
- [ ] `StudentController` extiende `BaseController` e implementa `MenuModule` con `position() == 1`. Submenú:
  1. Registrar estudiante
  2. Buscar estudiante
  3. Listar por grado
  4. Gestionar acudientes
  5. Matricular / cambiar de grado
  6. Cambiar estado
  7. Ver ficha completa
- [ ] Registrar en `AreaAModules.create(...)`.

Listo cuando: puedes registrar un estudiante con dos acudientes, matricularlo y verlo en el listado de su grado. Entradas inválidas se rechazan con mensaje y reintento.

PR 1: `dev-a` -> `develop` (módulo Estudiantes sin inscripción en materias todavía).

## Paso 3: Materias e inscripciones (módulo 1, cierre)

Tablas: `materias`, `inscripciones_materia`.

- [ ] Modelos: `Subject`, `SubjectEnrollment`.
- [ ] `SubjectDao`/`Impl`: `insert`, `findById`, `findByGrade(idGrado)`, `findActive()`.
- [ ] `SubjectEnrollmentDao`/`Impl`: `insert`, `findByStudent(idEstudiante, año, periodo)`, `findByCourse(idMateria, año, periodo)`, `exists(...)`, `updateStatus`.
- [ ] En `StudentService`: inscribir solo en materias del grado actual del estudiante, en un período I a IV, sin duplicar.
- [ ] Agregar la opción "Inscribir en materias" al submenú de Estudiantes.

Listo cuando: inscribes un estudiante en las materias de su grado y se rechaza una materia de otro grado.

## Paso 4: Profesores, asignaciones y horarios (módulo 3)

Tablas: `profesores`, `asignaciones_profesor`, `horarios_clase`.

- [ ] Modelos: `Teacher`, `TeachingAssignment`, `ClassSchedule`.
- [ ] `TeacherDao`/`Impl`: `insert`, `update`, `findById`, `findByDocument`, `findAllActive`, `updateStatus`.
- [ ] `AssignmentDao`/`Impl`: `insert`, `findByTeacher(idProfesor, año)`, `findBySubject(idMateria, año)`, `delete`; horarios: `insertSchedule`, `findSchedulesByTeacher`, `deleteSchedule`.
- [ ] `TeacherService`: documento y correo únicos; asignación sin duplicar (profesor, materia, año, sección); **conflicto de horario**: un profesor no puede tener dos clases que se traslapen el mismo día (`hora_inicio < fin_existente AND hora_fin > inicio_existente`); fin posterior a inicio.
- [ ] `TeacherView` y `TeacherController` (`position() == 3`). Submenú:
  1. Registrar profesor
  2. Listar profesores
  3. Asignar materia
  4. Registrar horario
  5. Ver carga académica
  6. Cambiar estado
- [ ] Registrar en `AreaAModules`.

Listo cuando: asignas una materia, registras su horario y el sistema rechaza un segundo horario que se traslapa.

PR 2: `dev-a` -> `develop` (Profesores y materias/inscripciones).

## Paso 5: Académica: notas y promedios (módulo 2)

Tabla: `registros_academicos`.

- [ ] Modelos: `AcademicRecord`, `AverageLine(String etiqueta, double promedio)`.
- [ ] `AcademicRecordDao`/`Impl`: `insert`, `update`, `find(idEstudiante, idMateria, año, periodo)`, `findByStudent(idEstudiante)` (historial), `findByCourse(idMateria, año, periodo)`, consultas de promedio con `AVG`.
- [ ] `AcademicService` (parte de notas): nota válida con `InputValidator.isValidGrade` (0.0 a 5.0, un decimal); el estudiante debe estar inscrito en la materia y el período; el profesor debe tener asignada la materia; una sola nota por estudiante, materia, año y período (si existe, pregunta si se actualiza).
- [ ] Promedios: por materia de cada estudiante, general del estudiante en un período, del curso por materia.
- [ ] Historial académico completo de un estudiante, ordenado por año y período.

Listo cuando: registras una nota 4.5, intentas 5.5 y 4.25 (rechazadas), y ves los tres promedios correctos contra un cálculo a mano.

## Paso 6: Académica: asistencia (módulo 2)

Tabla: `asistencias`.

- [ ] Modelo: `Attendance`.
- [ ] `AttendanceDao`/`Impl`: `insert`, `update`, `findByStudentAndRange`, `countAbsencesByWeek`, `findStudentsAboveThreshold(umbral)`.
- [ ] En `AcademicService`: una asistencia por estudiante, materia y fecha; estados Presente, Ausente, Tardanza, Excusa Médica; registro masivo por curso (lista los inscritos y pide el estado de cada uno).
- [ ] Alerta: estudiantes con más de `business.weeklyAbsenceThreshold` ausencias (solo "Ausente") en una semana; total de inasistencias acumuladas.
- [ ] `AcademicView` y `AcademicController` (`position() == 2`). Submenú:
  1. Registrar nota
  2. Ver historial académico
  3. Ver promedios
  4. Registrar asistencia del curso
  5. Reporte de inasistencias
- [ ] Registrar en `AreaAModules`.

Listo cuando: marcas 4 ausencias en una semana y el reporte lista al estudiante; con 3 no.

PR 3: `dev-a` -> `develop` (módulo Académica).

## Paso 7: Datos de prueba del Área A

- [ ] `docs/sql/10-extras-a.sql`: solo si cambiaste el esquema (con justificación). Si no, déjalo con su comentario.
- [ ] `docs/sql/11-data-a.sql`: 60 materias (5 por grado, códigos únicos); asignaciones y horarios para los 12 profesores sin traslapes; inscripciones del 2026; 300 o más notas; asistencias de varias semanas con algunos casos de más de 3 ausencias. Usa los IDs de `02-data-base.sql`.
- [ ] Probar la carga completa sobre una base limpia: 01, 02, 10, 11.

Listo cuando: el script carga sin errores y los promedios y reportes devuelven datos.

PR 4: `dev-a` -> `develop` (datos de prueba).

## Paso 8: Pulido y documentación

- [ ] Recorrer cada submenú con entradas inválidas: letras, fechas mal escritas, IDs inexistentes, campos vacíos, nota 5.5. Ningún error debe cerrar el programa.
- [ ] Apagar MySQL y probar una operación: mensaje claro, detalle en `logs/error_log.txt`.
- [ ] Revisar que no haya SQL concatenado, `System.out` directo fuera de las vistas, ni contraseñas.
- [ ] `docs/readme-a.md`: estructura, patrones (Singleton, DAO, MVC, inyección por constructor), manejo de errores y logs, funcionalidades de los módulos 1 a 3.

PR 5: `dev-a` -> `develop`.

## Paso 9: Cierre con Dev B

- [ ] Revisar los PR de Dev B (revisión cruzada) y pedir correcciones por issue.
- [ ] Probar el menú completo en `develop` con las 9 pruebas de [04-integracion-y-cierre.md](04-integracion-y-cierre.md).
- [ ] Generar `docs/database.sql` unificado (01, 02, 10, 11, 20, 21 en ese orden) y probarlo sobre una base limpia.
- [ ] `mvn clean package` y PR `develop` -> `main`. Dev B aprueba y pone la etiqueta `v1.0.0`.

## Resumen del orden

| Paso | Qué | Entrega |
|---|---|---|
| 0 | Preparación | Menú arranca |
| 1 | Modelos y DAOs de personas | DAOs probados |
| 2 | Módulo Estudiantes | PR 1 |
| 3 | Materias e inscripciones | Parte de PR 2 |
| 4 | Módulo Profesores y horarios | PR 2 |
| 5 | Notas y promedios | Parte de PR 3 |
| 6 | Asistencia | PR 3 |
| 7 | Datos de prueba | PR 4 |
| 8 | Pulido y README de A | PR 5 |
| 9 | Revisión cruzada y cierre | `main` |

Regla de oro: no importes clases del Área B ni toques el núcleo. Commits en español, sin líneas de atribución.
