package easystore.dao;

import easystore.model.SaleDetail;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDetailDAO {

    public void createWithConnection(Connection conn, SaleDetail d) throws SQLException {
        String sql = "INSERT INTO sale_details(sale_id, product_id, quantity, unit_price, total) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getSaleId());
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

    public List<SaleDetail> readBySaleId(int saleId) {
        List<SaleDetail> list = new ArrayList<>();
        String sql = "SELECT id, sale_id, product_id, quantity, unit_price, total FROM sale_details WHERE sale_id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SaleDetail d = new SaleDetail();
                    d.setId(rs.getInt("id"));
                    d.setSaleId(rs.getInt("sale_id"));
                    d.setProductId(rs.getInt("product_id"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getDouble("unit_price"));
                    d.setTotal(rs.getDouble("total"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read sale details", e);
        }
        return list;
    }
}
