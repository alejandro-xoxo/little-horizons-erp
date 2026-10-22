# Fase 0: Preparación (juntos, ~medio día)

Objetivo: que ambos puedan trabajar en paralelo sin bloquearse.

## Tareas conjuntas

- [ ] Crear el repo remoto vacío (GitHub) y conectar: `git remote add origin <url>`; `git push -u origin main`.
- [ ] Cada dev clona el repo y crea su rama: `dev-a` o `dev-b`.
- [ ] Instalar JDK 17, Maven 3.9+ y MySQL 8.0.16 o superior. Verificar: `java -version`, `mvn -v`, `mysql --version`.
- [ ] Leer [00-reglas-del-equipo.md](00-reglas-del-equipo.md).
- [ ] Acordar las firmas de las interfaces que B usará de A (ver tabla).

## Contrato inicial A -> B

| Interfaz (dueño A) | Métodos mínimos |
|---|---|
| `StudentDao` | `findById(int)`, `findAllActive()`, `findByDocument(String)` |
| `GuardianDao` | `findById(int)`, `findByStudent(int)`, `findPrincipalByStudent(int)`, `countStudentsByGuardian(int)` |
| `TeacherDao` | `findById(int)`, `findAllActive()` |

Los modelos `Student`, `Guardian` y `Teacher` (records) también los publica A en la Fase 1.

## Dev A

- [ ] Ejecutar el DDL oficial del anexo en MySQL: base `erp_little_horizons`.
- [ ] Agregar al DDL las tablas `horarios_clase` y `becas`, con justificación.
- [ ] Publicar en `main` el esqueleto (ya está: `pom.xml`, paquetes vacíos).

## Dev B

- [ ] Revisar el DDL y listar cualquier campo faltante para sus módulos (cupo de clubes, número de recibo, CUFE). Reportarlo a A antes de que termine la fase.
- [ ] Verificar que las dependencias de `pom.xml` bajan: `mvn clean compile` (PDFBox y ZXing).

## Criterio de salida

- Ambos compilan el esqueleto con `mvn clean compile`.
- La base `erp_little_horizons` existe en las dos máquinas.
- Interfaces del contrato acordadas por escrito (este documento).
