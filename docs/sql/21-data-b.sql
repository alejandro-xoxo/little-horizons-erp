-- Datos de prueba del Área B (Dev B): clubes, membresías, eventos, pagos, facturas, becas, recursos, préstamos. Puede asumir los IDs de 02-data-base.sql (estudiantes 1..48, acudientes 1..36, profesores 1..12).
-- Las fechas de préstamos y pagos son relativas al día de la carga (CURDATE()), así siempre hay vencidos y morosos.
-- Cargar UNA sola vez, después de 01-schema.sql y 02-data-base.sql (el código de recurso es único).
USE erp_little_horizons;

-- ============================================
-- PASO 1: RECURSOS Y PRÉSTAMOS (módulo 6)
-- Categorías: 1 Libro, 2 Equipo de Laboratorio, 3 Equipo Deportivo, 4 Dispositivo Tecnológico, 5 Material Didáctico
-- ============================================

INSERT INTO recursos (codigo_recurso, nombre_recurso, id_categoria, descripcion, fecha_adquisicion, ubicacion_fisica, estado_recurso) VALUES
('LIB-001', 'Cien años de soledad', 1, 'Novela de Gabriel García Márquez', '2022-02-10', 'Biblioteca', 'Prestado'),
('LIB-002', 'Don Quijote de la Mancha', 1, 'Edición escolar ilustrada', '2021-08-15', 'Biblioteca', 'Prestado'),
('LIB-003', 'Álgebra de Baldor', 1, 'Texto de álgebra para bachillerato', '2020-01-20', 'Biblioteca', 'Prestado'),
('LIB-004', 'Atlas Universal', 1, 'Atlas geográfico actualizado', '2023-03-05', 'Biblioteca', 'Disponible'),
('LIB-005', 'Diccionario de la Lengua Española', 1, 'Real Academia Española', '2022-06-18', 'Biblioteca', 'Disponible'),
('LIB-006', 'El Principito', 1, 'Antoine de Saint-Exupéry', '2023-09-01', 'Biblioteca', 'Disponible'),
('LIB-007', 'Física Conceptual', 1, 'Texto de física para décimo y once', '2021-04-12', 'Biblioteca', 'Disponible'),
('LIB-008', 'Química General', 1, 'Texto de química para bachillerato', '2021-04-12', 'Biblioteca', 'Disponible'),
('LIB-009', 'Historia de Colombia', 1, 'Compendio de historia nacional', '2022-11-30', 'Biblioteca', 'Disponible'),
('LIB-010', 'Cuentos de la Selva', 1, 'Horacio Quiroga, lectura de primaria', '2023-01-25', 'Biblioteca', 'Disponible'),
('LIB-011', 'Matemáticas 5', 1, 'Texto guía de quinto grado', '2024-02-02', 'Biblioteca', 'Disponible'),
('LIB-012', 'Ciencias Naturales 3', 1, 'Texto guía de tercer grado', '2024-02-02', 'Biblioteca', 'Disponible'),
('LIB-013', 'Inglés Beginner', 1, 'Libro de inglés nivel inicial', '2024-02-02', 'Biblioteca', 'Disponible'),
('LIB-014', 'Geografía de América', 1, 'Edición antigua, páginas deterioradas', '2015-05-09', 'Bodega', 'Dado de Baja'),
('LAB-001', 'Microscopio óptico 1', 2, 'Microscopio de 1000 aumentos', '2022-05-20', 'Laboratorio 1', 'Prestado'),
('LAB-002', 'Microscopio óptico 2', 2, 'Microscopio de 1000 aumentos', '2022-05-20', 'Laboratorio 1', 'Prestado'),
('LAB-003', 'Balanza analítica', 2, 'Precisión de 0.01 g', '2021-09-14', 'Laboratorio 1', 'Disponible'),
('LAB-004', 'Kit de química básica', 2, 'Tubos de ensayo, pipetas y soportes', '2023-02-28', 'Laboratorio 2', 'Disponible'),
('LAB-005', 'Kit de circuitos eléctricos', 2, 'Protoboard, resistencias y LEDs', '2023-02-28', 'Laboratorio 2', 'Disponible'),
('LAB-006', 'Telescopio escolar', 2, 'Telescopio refractor 70 mm', '2020-10-03', 'Laboratorio 2', 'Disponible'),
('LAB-007', 'Multímetro digital', 2, 'En revisión por falla en la pantalla', '2021-07-07', 'Laboratorio 2', 'En Mantenimiento'),
('LAB-008', 'Modelo de célula', 2, 'Modelo desarmable de célula animal', '2019-03-11', 'Laboratorio 1', 'Disponible'),
('DEP-001', 'Balón de fútbol 1', 3, 'Balón número 5', '2024-01-15', 'Coliseo', 'Prestado'),
('DEP-002', 'Balón de fútbol 2', 3, 'Balón número 5', '2024-01-15', 'Coliseo', 'Disponible'),
('DEP-003', 'Balón de baloncesto', 3, 'Balón oficial número 7', '2023-08-22', 'Coliseo', 'Disponible'),
('DEP-004', 'Balón de voleibol', 3, 'Balón de competencia', '2023-08-22', 'Coliseo', 'Disponible'),
('DEP-005', 'Juego de conos', 3, 'Doce conos de entrenamiento', '2022-03-01', 'Coliseo', 'Disponible'),
('DEP-006', 'Cuerdas para saltar', 3, 'Diez cuerdas', '2022-03-01', 'Coliseo', 'Disponible'),
('DEP-007', 'Red de voleibol', 3, 'Red reglamentaria', '2021-11-19', 'Coliseo', 'Disponible'),
('DEP-008', 'Colchoneta de gimnasia', 3, 'Rota, no se puede reparar', '2016-06-30', 'Bodega', 'Dado de Baja'),
('TEC-001', 'Tablet educativa 1', 4, 'Tablet de 10 pulgadas con licencias', '2024-03-12', 'Sala de sistemas', 'Prestado'),
('TEC-002', 'Tablet educativa 2', 4, 'Tablet de 10 pulgadas con licencias', '2024-03-12', 'Sala de sistemas', 'Prestado'),
('TEC-003', 'Calculadora científica 1', 4, 'Casio fx-991', '2023-05-17', 'Sala de sistemas', 'Disponible'),
('TEC-004', 'Calculadora científica 2', 4, 'Casio fx-991', '2023-05-17', 'Sala de sistemas', 'Disponible'),
('TEC-005', 'Portátil de préstamo 1', 4, 'Portátil de 14 pulgadas', '2024-06-10', 'Sala de sistemas', 'Disponible'),
('TEC-006', 'Portátil de préstamo 2', 4, 'Portátil de 14 pulgadas', '2024-06-10', 'Sala de sistemas', 'Disponible'),
('TEC-007', 'Proyector portátil', 4, 'Proyector HDMI de 3000 lúmenes', '2022-12-05', 'Sala de sistemas', 'Disponible'),
('TEC-008', 'Cámara de documentos', 4, 'No enciende, enviada a revisión', '2021-02-14', 'Sala de sistemas', 'En Mantenimiento'),
('DID-001', 'Mapa de Colombia en relieve', 5, 'Mapa físico de pared', '2020-02-18', 'Sala de profesores', 'Prestado'),
('DID-002', 'Modelo del sistema solar', 5, 'Maqueta con planetas desmontables', '2021-10-27', 'Sala de profesores', 'Disponible'),
('DID-003', 'Tangram de madera', 5, 'Veinte juegos', '2022-04-04', 'Sala de profesores', 'Disponible'),
('DID-004', 'Bloques lógicos', 5, 'Caja de 60 piezas', '2022-04-04', 'Sala de profesores', 'Disponible'),
('DID-005', 'Esqueleto humano a escala', 5, 'Modelo de 85 cm', '2020-09-09', 'Sala de profesores', 'Disponible'),
('DID-006', 'Globo terráqueo', 5, 'Globo político de 30 cm', '2021-01-21', 'Sala de profesores', 'Disponible');

-- Préstamos de estudiantes sin devolver (los recursos correspondientes quedaron en estado Prestado arriba).
-- LIB-002, LIB-003 y DEP-001 llevan fecha esperada pasada; LIB-003 sigue en 'Activo' para probar el marcado a Vencido.
INSERT INTO prestamos_estudiante (id_recurso, id_estudiante, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado_prestamo, observaciones) VALUES
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-001'), 1,  DATE_SUB(CURDATE(), INTERVAL 3 DAY),  DATE_ADD(CURDATE(), INTERVAL 4 DAY),  NULL, 'Activo', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-002'), 5,  DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 13 DAY), NULL, 'Vencido', 'Lectura del plan lector'),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-003'), 12, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 8 DAY),  NULL, 'Activo', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'TEC-001'), 20, DATE_SUB(CURDATE(), INTERVAL 2 DAY),  DATE_ADD(CURDATE(), INTERVAL 5 DAY),  NULL, 'Activo', 'Trabajo de investigación'),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'DEP-001'), 33, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY),  NULL, 'Vencido', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LAB-001'), 40, DATE_SUB(CURDATE(), INTERVAL 1 DAY),  DATE_ADD(CURDATE(), INTERVAL 6 DAY),  NULL, 'Activo', 'Feria de ciencias');

-- Préstamos de estudiantes ya devueltos (historial).
INSERT INTO prestamos_estudiante (id_recurso, id_estudiante, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado_prestamo, observaciones) VALUES
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-004'), 8,  DATE_SUB(CURDATE(), INTERVAL 40 DAY), DATE_SUB(CURDATE(), INTERVAL 33 DAY), DATE_SUB(CURDATE(), INTERVAL 34 DAY), 'Devuelto', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-005'), 15, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 23 DAY), DATE_SUB(CURDATE(), INTERVAL 20 DAY), 'Devuelto', 'Devuelto con tres días de retraso'),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-006'), 22, DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_SUB(CURDATE(), INTERVAL 19 DAY), 'Devuelto', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'DEP-002'), 3,  DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY),  DATE_SUB(CURDATE(), INTERVAL 6 DAY),  'Devuelto', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'TEC-003'), 44, DATE_SUB(CURDATE(), INTERVAL 18 DAY), DATE_SUB(CURDATE(), INTERVAL 11 DAY), DATE_SUB(CURDATE(), INTERVAL 12 DAY), 'Devuelto', 'Examen de matemáticas');

-- Préstamos de profesores. LAB-002 vencido; DID-001 con fecha pasada pero aún 'Activo'.
INSERT INTO prestamos_profesor (id_recurso, id_profesor, fecha_prestamo, fecha_devolucion_esperada, fecha_devolucion_real, estado_prestamo, observaciones) VALUES
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'TEC-002'), 3, DATE_SUB(CURDATE(), INTERVAL 4 DAY),  DATE_ADD(CURDATE(), INTERVAL 3 DAY),  NULL, 'Activo', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LAB-002'), 7, DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY),  NULL, 'Vencido', 'Práctica de biología'),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'DID-001'), 2, DATE_SUB(CURDATE(), INTERVAL 9 DAY),  DATE_SUB(CURDATE(), INTERVAL 2 DAY),  NULL, 'Activo', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LIB-007'), 1, DATE_SUB(CURDATE(), INTERVAL 35 DAY), DATE_SUB(CURDATE(), INTERVAL 28 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY), 'Devuelto', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'LAB-003'), 4, DATE_SUB(CURDATE(), INTERVAL 22 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 'Devuelto', NULL),
((SELECT id_recurso FROM recursos WHERE codigo_recurso = 'DID-002'), 9, DATE_SUB(CURDATE(), INTERVAL 16 DAY), DATE_SUB(CURDATE(), INTERVAL 9 DAY),  DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'Devuelto', NULL);
