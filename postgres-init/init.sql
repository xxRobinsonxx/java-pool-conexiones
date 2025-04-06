CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2),
    stock INT
);

INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
('Producto 1', 'Descripción del producto 1', 10.99, 100),
('Producto 2', 'Descripción del producto 2', 15.49, 50),
('Producto 3', 'Descripción del producto 3', 20.00, 200);
