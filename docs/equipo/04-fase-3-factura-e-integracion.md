# Fase 3: Factura electrónica e integración

> **Ramas:** Dev A trabaja en `dev-a`, Dev B en `dev-b`. Cada uno entrega con PR a `develop`. Al cerrar la fase, PR `develop` -> `main`.
> **Antes de empezar:** `git checkout dev-x` y `git pull --rebase origin develop`.

Objetivo: factura PDF al confirmar un pago y aplicación completa funcionando desde el menú principal.

## Dev B: factura (módulo 3.3)

- [ ] `util/PdfGenerator` (PDFBox) con: razón social "Colegio Internacional Little Horizons S.A.S.", NIT, dirección, teléfono, correo.
- [ ] Datos del cliente: nombre del acudiente responsable, documento, dirección.
- [ ] Detalle: número consecutivo, fecha de emisión, ítems (descripción, cantidad, valor unitario, subtotal), subtotal, IVA si aplica, total.
- [ ] Cumplimiento DIAN simulado: CUFE = SHA-256(número de factura + NIT + fecha), código QR (ZXing) con los datos de la factura, resolución de habilitación simulada.
- [ ] Al confirmar un pago: `FinanceService` reúne los datos, calcula el CUFE, genera `facturas/factura_000123.pdf` con nombre único y guarda `numero_factura`, `cufe`, `nombre_archivo_pdf` y `ruta_archivo` en la tabla `facturas`.
- [ ] La vista muestra la ruta donde quedó el PDF.
- [ ] Datos del colegio y resolución leídos de `config.properties` (claves `school.*`).

## Dev A: integración

- [ ] Cablear en `Main` todos los controladores y servicios con inyección por constructor.
- [ ] `MainController` y `MainMenuView`: menú principal con los 7 módulos y salir, como en el ejemplo del enunciado.
- [ ] Consola UTF-8 en Windows (`chcp 65001`) para ver los marcos Unicode.
- [ ] Revisar de punta a punta mensajes, validaciones y navegación de los módulos de B (revisión cruzada).
- [ ] Mientras B termina la factura: dejar el menú con stubs de los módulos de B y cablearlos a medida que lleguen sus PRs.
- [ ] Escribir las secciones del README que son de A (6.1 cambios al DDL, 8 estructura, 9 patrones, 11 errores y logs) en `docs/readme-a.md` y pasárselas a B.

## Revisión cruzada de B sobre A

- [ ] B revisa los módulos de A (Estudiantes, Académico, Profesores): validaciones, mensajes, navegación y reglas de negocio. Los hallazgos se abren como issues para A.

## Criterio de salida

- Se paga un concepto y se genera un PDF abrible con todos los datos y el QR.
- Todos los módulos son accesibles desde el menú principal.
- Ninguna excepción cierra el programa.
