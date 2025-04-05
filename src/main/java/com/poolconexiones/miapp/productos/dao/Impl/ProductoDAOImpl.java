package com.poolconexiones.miapp.productos.dao.Impl;

import com.poolconexiones.miapp.productos.dao.ProductoDAO;
import com.poolconexiones.miapp.productos.model.Producto;
import com.poolconexiones.miapp.productos.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductoDAOImpl implements ProductoDAO {

    private final JdbcTemplate jdbcTemplate;


    // Mapeo de filas a objetos Producto
    private final RowMapper<Producto> productoRowMapper = (rs, rowNum) -> {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        return producto;
    };

    @Autowired
    public ProductoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Producto crear(Producto producto) {
        try {
            String sql = "INSERT INTO productos (nombre, descripcion, precio, stock) " +
                    "VALUES (?, ?, ?, ?) RETURNING id";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setBigDecimal(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                return ps;
            }, keyHolder);

            Long id = keyHolder.getKey().longValue();
            producto.setId(id);

            return producto;
        } catch (Exception e) {
            throw new DatabaseException("Error al crear producto", e);
        }
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        try {
            String sql = "SELECT * FROM productos WHERE id = ?";
            List<Producto> productos = jdbcTemplate.query(sql, productoRowMapper, id);

            return productos.isEmpty() ? Optional.empty() : Optional.of(productos.get(0));
        } catch (Exception e) {
            throw new DatabaseException("Error al obtener producto por ID", e);
        }
    }

    @Override
    public List<Producto> listarTodos() {
        try {
            String sql = "SELECT * FROM productos ORDER BY id";
            return jdbcTemplate.query(sql, productoRowMapper);
        } catch (Exception e) {
            throw new DatabaseException("Error al listar productos", e);
        }
    }

    @Override
    public Producto actualizar(Producto producto) {
        try {
            String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, " +
                    " WHERE id = ? ";

            int filasAfectadas = jdbcTemplate.update(sql,
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getId());

            if (filasAfectadas == 0) {
                throw new DatabaseException("No se pudo actualizar el producto con ID: " + producto.getId());
            }

            return producto;
        } catch (Exception e) {
            throw new DatabaseException("Error al actualizar producto", e);
        }
    }


}
