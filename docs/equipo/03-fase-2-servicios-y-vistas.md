# Fase 2: Servicios, controladores y vistas (en paralelo)

Objetivo: cada módulo funcional de punta a punta (vista -> controlador -> servicio -> DAO) y probable de forma aislada.

## Dev A: módulos 1, 2 y 3

Estudiantes y matrícula (`StudentService`, `StudentController`, `StudentView`):
- [ ] Registro completo de estudiantes con validación (documento único, fecha de nacimiento).
- [ ] Acudientes con relación N:M (hermanos); al menos un acudiente por estudiante.
- [ ] Matrícula por grado y año académico con estado (Activo, Graduado, Retirado); cambio de grado y de estado.
- [ ] Inscripción en materias por período, solo materias del grado del estudiante.

Académico (`AcademicService`, `AcademicController`, `AcademicView`):
- [ ] Ingreso de notas 0.0 a 5.0 con un decimal, períodos I, II, III, IV y Final. Valida que el profesor esté asignado y el estudiante inscrito.
- [ ] Promedios automáticos: por materia del estudiante, general del estudiante en el período, del curso por materia.
- [ ] Historial académico completo de un estudiante.
- [ ] Asistencia diaria por materia (Presente, Ausente, Tardanza, Excusa Médica).
- [ ] Reporte de inasistencias: más de 3 ausencias en una semana (umbral configurable).

Profesores (`TeacherService`, `TeacherController`, `TeacherView`):
- [ ] Directorio de profesores.
- [ ] Asignación de materias (un profesor, varias materias; una materia, varios profesores).
- [ ] Horarios de clase con detección de conflictos (mismo profesor, mismo día y hora).

## Dev B: módulos 4, 5 y 6

Extracurriculares (`ExtracurricularService`, `ExtracurricularController`, `ExtracurricularView`):
- [ ] Catálogo de clubes con cupo máximo y profesor encargado.
- [ ] Inscripción en clubes validando cupo; retiro con motivo y fecha.
- [ ] Eventos (Académico, Cultural, Deportivo, Social, Científico), participantes y posición en eventos con competencia.

Financiero, sin factura todavía (`FinanceService`, `FinanceController`, `FinanceView`):
- [ ] Catálogo de conceptos de pago.
- [ ] Valor personalizado por estudiante (becas parciales, descuento por hermanos; se aplica el mayor, no se suman).
- [ ] Registro de pagos: estudiante, concepto, monto, fecha, método y número de recibo consecutivo.
- [ ] Historial de pagos por estudiante y por acudiente.
- [ ] Reporte de morosos (pendientes por más de un mes).

Recursos (`ResourceService`, `ResourceController`, `ResourceView`):
- [ ] Inventario con código único, categoría, estado y ubicación.
- [ ] Préstamo a estudiante o profesor (tablas separadas); el recurso pasa a "Prestado".
- [ ] Devolución: fecha real y recurso a "Disponible". Transaccional.
- [ ] Reporte de préstamos vencidos.

## Reglas de la fase

- Los servicios reciben los DAOs por constructor (para poder probarlos).
- B usa `StudentDao`, `GuardianDao` y `TeacherDao` solo por interfaz.
- Cada controlador se puede probar con un `main` temporal propio; el cableado definitivo en `Main` lo hace A en la Fase 3.

## Criterio de salida

- Cada módulo cumple el punto 5.2 del enunciado: validación con reintento, mensajes de éxito o error, volver al menú.
- PRs aprobados por el otro dev y mezclados en `main`.
