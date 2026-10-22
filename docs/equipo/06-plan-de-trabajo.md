# Plan de trabajo: orden de ejecución

Cada paso tiene un número. "Depende de" indica qué debe estar mezclado en `develop` antes de empezar. Los pasos de A y de B sin dependencia se hacen en paralelo.

Leyenda: **A** = Dev A (rama `dev-a`), **B** = Dev B (rama `dev-b`), **AB** = los dos.

## Fase 0: Preparación

| # | Quién | Tarea | Depende de | Entrega |
|---|---|---|---|---|
| 0.1 | AB | Instalar requisitos, clonar, cambiar a su rama, configurar `LH_DB_PASSWORD` | - | `mvn clean compile` compila |
| 0.2 | AB | Leer reglas y contratos (00 y 01) | - | Dudas resueltas |
| 0.3 | A | Escribir `docs/sql/01-schema.sql` (DDL oficial + `horarios_clase`) y PR a `develop` | - | `01-schema.sql` en `develop` |
| 0.4 | B | Redactar `docs/sql/02-extras-b.sql` (`becas`, cupo, recibo, CUFE) y ajustar `.gitignore` | 0.3 | PR a `develop` |
| 0.5 | AB | Crear la base `erp_little_horizons` con 01 y 02 | 0.3, 0.4 | Base creada en las dos máquinas |

**B espera a A** en 0.4 (necesita ver el DDL). Mientras tanto, B instala herramientas y revisa el enunciado.

## Fase 1: Base, modelos y DAOs

| # | Quién | Tarea | Depende de | Entrega |
|---|---|---|---|---|
| 1.1 | B | PR de `util/`: `CustomExceptions`, `LoggerUtility`, `InputValidator` | 0.1 | En `develop` (corto, primero) |
| 1.2 | A | Escribir `config/`, `config.properties`, `BaseJdbcDao`, `BaseView`, `BaseController` | - (usa las firmas del contrato) | PR 1 de A |
| 1.3 | A | Mezclar el PR 1 | 1.1 | Infraestructura en `develop` |
| 1.4 | A | Modelos y DAOs de Student, Guardian, Teacher, Enrollment, GradeLevel | 1.3 | PR 2 de A en `develop` |
| 1.5 | B | Modelos de Club, SchoolEvent, PaymentConcept, Payment, Invoice, Scholarship, ResourceCategory, Resource, Loan | 0.1 | PR en `develop` |
| 1.6 | B | `docs/sql/03-data.sql` completo | 0.5 | PR en `develop` |
| 1.7 | B | DAOs de Club, Event, Payment, Invoice, Scholarship, Resource, Loan, Report | 1.3 y 1.4 | PR en `develop` |
| 1.8 | A | Modelos y DAOs de Subject, SubjectEnrollment, AcademicRecord, Attendance, Assignment | 1.3 | PR 3 de A en `develop` |
| 1.9 | AB | Probar DAOs contra la base con datos reales | 1.6, 1.7, 1.8 | `mvn clean compile` OK |

**Camino crítico:** 1.1 -> 1.3 -> 1.4. Si se atrasa, se atrasa B.
**A espera a B** en 1.3 (necesita las excepciones para mezclar). **B espera a A** en 1.7 (necesita 1.3 y 1.4). Mientras espera, B hace 1.5 y 1.6; A hace 1.8.

## Fase 2: Servicios, controladores y vistas

| # | Quién | Tarea | Depende de | Entrega |
|---|---|---|---|---|
| 2.1 | A | Estudiantes y matrícula (service, controller, view) | 1.4 | PR en `develop` |
| 2.2 | A | Profesores y horarios | 1.8 | PR en `develop` |
| 2.3 | A | Académico: notas, promedios, asistencia | 2.1, 2.2 | PR en `develop` |
| 2.4 | B | Extracurriculares: clubes con cupo, eventos | 1.7 | PR en `develop` |
| 2.5 | B | Recursos: inventario, préstamos, devoluciones, vencidos | 1.7 | PR en `develop` |
| 2.6 | B | Financiero sin factura: conceptos, pagos, becas, hermanos, morosos | 1.7, 2.1 | PR en `develop` |
| 2.7 | AB | Anotar en cada PR los constructores públicos | - | Lista para cablear |

2.4 y 2.5 solo necesitan los DAOs, así que B las hace primero; 2.6 va al final porque necesita Student y Guardian de A (2.1). Con mocks de interfaz B no se bloquea.
Dentro de A el orden es: Estudiantes -> Profesores -> Académico (Académico necesita Teacher, Assignment y SubjectEnrollment).

## Fase 3: Factura e integración

| # | Quién | Tarea | Depende de | Entrega |
|---|---|---|---|---|
| 3.1 | B | `util/pdf/PdfGenerator` con CUFE, QR y datos del colegio | 1.3 (config) | PR en `develop` |
| 3.2 | B | Generar factura al confirmar pago y guardar en tabla `facturas` | 2.6, 3.1 | PR en `develop` |
| 3.3 | A | Menú principal y `Main` con los módulos de A y stubs de B | 2.1 a 2.3 | PR en `develop` |
| 3.4 | A | Cablear los módulos de B según los constructores anotados | 2.4, 2.5, 3.2 | PR en `develop` |
| 3.5 | A | Escribir `docs/readme-a.md` y pasarlo a B | - | Archivo en `develop` |
| 3.6 | B | Revisión cruzada de los módulos de A; issues para A | 2.1 a 2.3 | Issues abiertos |
| 3.7 | A | Revisión cruzada de los módulos de B; issues para B | 2.4 a 2.6, 3.2 | Issues abiertos |

**A espera a B** en 3.4. Mientras tanto A hace 3.3, 3.5 y 3.7 parcial.

## Fase 4: Reportes y cierre

| # | Quién | Tarea | Depende de | Entrega |
|---|---|---|---|---|
| 4.1 | B | Módulo de reportes (service, controller, view) | 1.7, datos reales | PR en `develop` |
| 4.2 | A | Conectar reportes al menú | 4.1 | PR en `develop` |
| 4.3 | A | Revisar errores, SQL y credenciales; generar `docs/database.sql` unificado | 3.4 | PR en `develop` |
| 4.4 | B | Ensamblar README completo con `docs/readme-a.md` | 3.5 | PR en `develop` |
| 4.5 | AB | Pruebas cruzadas (lista en el doc 05) y corrección de hallazgos | 4.2 | Sin fallos abiertos |
| 4.6 | A | PR `develop` -> `main`, `mvn clean package` | 4.5 | Jar generado |
| 4.7 | B | Aprobar PR y etiquetar `git tag v1.0.0` | 4.6 | Entrega |

**A espera a B** en 4.2. Mientras tanto A hace 4.3.

## Resumen: quién espera a quién

| Momento | Espera | A quién | Hasta |
|---|---|---|---|
| 0.4 | B | A | `01-schema.sql` en `develop` |
| 1.3 | A | B | PR de `util/` en `develop` (horas) |
| 1.7 | B | A | PR 1 y PR 2 de A en `develop` |
| 2.6 | B | A | `Student` y `Guardian` de A (servicio y DAO) |
| 3.4 | A | B | Controladores de B y la factura |
| 4.2 | A | B | Módulo de reportes |

## Reparto de carga resultante

| | Fase 0-1 | Fase 2 | Fase 3 | Fase 4 |
|---|---|---|---|---|
| **A** | Esquema, infraestructura, 5+5 modelos y DAOs | Estudiantes, Profesores, Académico | Menú, cableado, README parcial, revisión | Conexión, calidad, `database.sql`, release |
| **B** | Utilidades, 9 modelos, 8 DAOs, datos de prueba | Clubes, Eventos, Recursos, Finanzas | Factura PDF, revisión | Reportes, README, etiqueta |
