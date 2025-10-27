package easystore.dao;

import easystore.model.Purchase;
import easystore.model.PurchaseDetail;
import easystore.exceptions.DataAccessException;
import easystore.model.Supplier;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    private PurchaseDetailDAO detailDAO = new PurchaseDetailDAO();
    private ProductDAO productDAO = new ProductDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();

    public void create(Purchase p, String supplierName) {
        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Supplier supplier = supplierDAO.readByName(supplierName);
                if (supplier == null) {
                    throw new SQLException("Supplier not found: " + supplierName);
                }
                String sql = "INSERT INTO purchases(supplier_id, date, total_amount) VALUES(?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, supplier.getId());
                    ps.setString(2, p.getDate() == null ? LocalDate.now().toString() : p.getDate().toString());
                    ps.setDouble(3, p.getTotalAmount());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            p.setId(keys.getInt(1));
                        }
                    }
                }

                for (PurchaseDetail d : p.getDetails()) {
                    detailDAO.createWithConnection(conn, d);
                    String upd = "UPDATE products SET stock = stock + ? WHERE id = ?";
                    try (PreparedStatement psu = conn.prepareStatement(upd)) {
                        psu.setInt(1, d.getQuantity());
                        psu.setInt(2, d.getProductId());
                        psu.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create purchase", e);
        }
    }

    public List<Purchase> readAll() {
        List<Purchase> list = new ArrayList<>();
        String sql = "SELECT id, supplier_id, date, total_amount FROM purchases";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Purchase p = new Purchase();
                p.setId(rs.getInt("id"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setDate(LocalDate.parse(rs.getString("date")));
                p.setTotalAmount(rs.getDouble("total_amount"));
                list.add(p);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read purchases", e);
        }
        return list;
    }
}
