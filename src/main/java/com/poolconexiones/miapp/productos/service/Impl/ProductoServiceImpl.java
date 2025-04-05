package com.poolconexiones.miapp.productos.service.Impl;

import com.poolconexiones.miapp.productos.dao.ProductoDAO;
import com.poolconexiones.miapp.productos.dto.ProductoDTO;
import com.poolconexiones.miapp.productos.model.Producto;
import com.poolconexiones.miapp.productos.service.ProductoService;
import com.poolconexiones.miapp.productos.exception.ProductoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDAO productoDAO;

    @Autowired
    public ProductoServiceImpl(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    // Método para convertir de Entidad a DTO
    private ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    // Método para convertir de DTO a Entidad
    private Producto convertirAEntidad(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        return producto;
    }

    @Override
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = convertirAEntidad(productoDTO);
        Producto nuevoProducto = productoDAO.crear(producto);
        return convertirADTO(nuevoProducto);
    }

    @Override
    public Optional<ProductoDTO> obtenerProductoPorId(Long id) {
        return productoDAO.obtenerPorId(id)
                .map(this::convertirADTO);
    }

    @Override
    public List<ProductoDTO> listarProductos() {
        return productoDAO.listarTodos()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        // Verificar si el producto existe
        Producto productoExistente = productoDAO.obtenerPorId(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        // Actualizar los campos
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setStock(productoDTO.getStock());

        // Guardar los cambios
        Producto productoActualizado = productoDAO.actualizar(productoExistente);

        return convertirADTO(productoActualizado);
    }
}