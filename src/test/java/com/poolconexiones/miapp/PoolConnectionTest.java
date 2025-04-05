package com.poolconexiones.miapp;
import com.poolconexiones.miapp.productos.dao.Impl.ProductoDAOImpl;
import com.poolconexiones.miapp.productos.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;

@SpringBootTest
public class PoolConnectionTest {

    @Autowired
    private ProductoDAOImpl productoDAO;

    @Test
    public void testPoolDeConexiones() throws InterruptedException {
        int numHilos = 20000;
        ExecutorService executorService = Executors.newFixedThreadPool(numHilos);

        for (int i = 0; i < numHilos; i++) {
            int finalI = i;
            executorService.submit(() -> {
                Producto producto = new Producto(null, "Producto " + finalI, "Descr. Producto " + finalI, BigDecimal.valueOf(100.0 + finalI), 5);
                Producto creado = productoDAO.crear(producto);
                System.out.println("Producto creado: ID " + creado.getId());
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Prueba de carga finalizada.");
    }
}
