package easystore.controller;

import easystore.dao.SaleDAO;
import easystore.model.Sale;

public class SaleController {

    private final SaleDAO saleDAO;

    public SaleController(SaleDAO saleDAO) {
        this.saleDAO = saleDAO;
    }

    public void createSale(Sale s, String customerName) {
        saleDAO.create(s, customerName);
    }

    public java.util.List<easystore.model.Sale> listSales() {
        return saleDAO.readAll();
    }

    public void deleteById(int id) {
        saleDAO.deleteById(id);
    }
}
