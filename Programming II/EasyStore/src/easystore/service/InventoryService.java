package easystore.service;

import easystore.dao.ProductDAO;
import easystore.model.Product;
import java.util.List;

public class InventoryService implements Reportable {

    private final ProductDAO productDAO;

    public InventoryService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> listProducts() {
        return productDAO.readAll();
    }

    public Product findByName(String name) {
        return productDAO.readByName(name);
    }

    @Override
    public void generateReport() {
        System.out.println("--- Reporte de Inventario ---");
        for (Product p : productDAO.readAll()) {
            System.out.println(p.getName() + " | stock=" + p.getStock() + " | Precio Venta=" + p.getSalesPrice());
        }
    }
}
