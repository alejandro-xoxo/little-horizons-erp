# Fase 0: Preparación (juntos, ~medio día)

Objetivo: que ambos puedan trabajar en paralelo sin bloquearse.

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

- [ ] Cada dev clona el repo y cambia a su rama (`dev-a` o `dev-b`).
- [ ] Confirmar que `main`, `develop`, `dev-a` y `dev-b` existen en GitHub.
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
