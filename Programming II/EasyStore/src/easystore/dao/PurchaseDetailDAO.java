package easystore.dao;

import easystore.model.PurchaseDetail;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailDAO {

    public void createWithConnection(Connection conn, PurchaseDetail d) throws SQLException {
        String sql = "INSERT INTO purchase_details(purchase_id, product_id, quantity, unit_price, total) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getPurchaseId());
            ps.setInt(2, d.getProductId());
            ps.setInt(3, d.getQuantity());
            ps.setDouble(4, d.getUnitPrice());
            ps.setDouble(5, d.calculateAmount());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    d.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<PurchaseDetail> readByPurchaseId(int purchaseId) {
        List<PurchaseDetail> list = new ArrayList<>();
        String sql = "SELECT id, purchase_id, product_id, quantity, unit_price, total FROM purchase_details WHERE purchase_id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, purchaseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseDetail d = new PurchaseDetail();
                    d.setId(rs.getInt("id"));
                    d.setPurchaseId(rs.getInt("purchase_id"));
                    d.setProductId(rs.getInt("product_id"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getDouble("unit_price"));
                    d.setTotal(rs.getDouble("total"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read purchase details", e);
        }
        return list;
    }
}
