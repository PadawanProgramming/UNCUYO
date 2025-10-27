package easystore.controller;

import easystore.dao.ConnectionManager;
import easystore.dao.PurchaseDAO;
import easystore.exceptions.DataAccessException;
import easystore.model.Purchase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PurchaseController {

    private final PurchaseDAO purchaseDAO;

    public PurchaseController(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
    }

    public void createPurchase(Purchase p, String supplierName) {
        purchaseDAO.create(p, supplierName);
    }

    public java.util.List<easystore.model.Purchase> listPurchases() {
        return purchaseDAO.readAll();
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM sales WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete sale", e);
        }
    }
}
