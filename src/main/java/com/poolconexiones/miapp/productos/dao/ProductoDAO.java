package com.poolconexiones.miapp.productos.dao;

import com.poolconexiones.miapp.productos.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoDAO {
    Producto crear(Producto producto);
    Optional<Producto> obtenerPorId(Long id);
    List<Producto> listarTodos();
    Producto actualizar(Producto producto);
}
