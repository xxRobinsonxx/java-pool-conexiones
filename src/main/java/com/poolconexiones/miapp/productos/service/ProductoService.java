package com.poolconexiones.miapp.productos.service;

import com.poolconexiones.miapp.productos.dto.ProductoDTO;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    ProductoDTO crearProducto(ProductoDTO productoDTO);
    Optional<ProductoDTO> obtenerProductoPorId(Long id);
    List<ProductoDTO> listarProductos();
    ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO);
}
