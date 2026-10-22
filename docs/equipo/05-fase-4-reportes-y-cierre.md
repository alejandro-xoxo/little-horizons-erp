# Fase 4: Reportes, pruebas cruzadas y cierre

> **Ramas:** Dev A trabaja en `dev-a`, Dev B en `dev-b`. Cada uno entrega con PR a `develop`. Al cerrar la fase, PR `develop` -> `main`.
> **Antes de empezar:** `git checkout dev-x` y `git pull --rebase origin develop`.

Objetivo: módulo 7 completo, sistema probado entre los dos y entrega lista.

## Dev B: reportes (módulo 7)

- [ ] `ReportService`, `ReportController`, `ReportView` sobre `ReportDao`.
- [ ] Estudiantes por grado.
- [ ] Mejores promedios.
- [ ] Participación en clubes y eventos.
- [ ] Ingresos por concepto y por mes.
- [ ] Estado de los recursos y préstamos vencidos.
- [ ] README completo: título, descripción, autores, tecnologías, requisitos, configuración de BD, instalación y ejecución, estructura, patrones, funcionalidades, errores y logs, limitaciones, licencia.

## Dev A: calidad y cierre

- [ ] Conectar el módulo 7 en el menú principal (cuando B lo entregue).
- [ ] Verificar manejo de errores: `BaseController.safely` captura todo, `logs/error_log.txt` guarda fecha, contexto y stacktrace.
- [ ] Revisar que no exista SQL concatenado ni credenciales en el código.
- [ ] Revisar `.gitignore` y que `main` compile desde cero: `mvn clean package`.

## Juntos

- [ ] Pruebas cruzadas: cada dev prueba el módulo del otro con la lista de abajo.
- [ ] Corregir hallazgos por issue y PR.
- [ ] Etiqueta final: `git tag v1.0.0` en `main`.

## Lista de pruebas cruzadas

1. Matricular un estudiante con dos acudientes y un hermano; verificar descuento por hermanos en la pensión.
2. Registrar notas de los cuatro períodos y comprobar los promedios.
3. Marcar 4 ausencias en una semana y ver la alerta.
4. Intentar inscribir en un club lleno: debe rechazarse.
5. Registrar un pago y abrir el PDF de la factura (CUFE y QR presentes).
6. Prestar un recurso, intentar prestarlo otra vez (debe rechazarse), devolverlo.
7. Reporte de morosos y de préstamos vencidos con datos reales.
8. Entradas inválidas en cada menú: letras donde van números, fechas mal escritas, nota 5.5.

## Criterio de salida

- Lista de pruebas sin fallos abiertos.
- `mvn clean package` genera el jar y arranca contra una base limpia creada con `docs/database.sql`.
- README completo y etiqueta `v1.0.0`.
