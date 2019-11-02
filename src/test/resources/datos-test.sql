/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento) VALUES('1', 'ana.garcia@gmail.com', 'Ana García', '12345678', '2001-02-10');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, tipo) VALUES('2', 'juan.lopez@gmail.com', 'Juan López', '12345678', '2001-02-10', 'admin');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, tipo) VALUES('3', 'paco.perez@gmail.com', 'Paco Pérez', '12345678', '2001-02-10', 'usuario');
INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto Cobalto');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');