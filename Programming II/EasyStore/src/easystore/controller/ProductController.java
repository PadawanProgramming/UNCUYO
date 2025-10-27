package easystore.controller;

import easystore.dao.ProductDAO;
import easystore.model.Product;
import easystore.exceptions.NotFoundException;

public class ProductController {

    private final ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void createProduct(Product p) {
        productDAO.create(p);
    }

    public Product getByName(String name) throws NotFoundException {
        Product p = productDAO.readByName(name);
        if (p == null) {
            throw new NotFoundException("Product not found: " + name);
        }
        return p;
    }

    public void updateProduct(Product p) {
        productDAO.update(p);
    }

    public void deleteByName(String name) {
        productDAO.deleteByName(name);
    }
}
