package easystore.dao;

import easystore.model.Sale;
import easystore.model.SaleDetail;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    private SaleDetailDAO detailDAO = new SaleDetailDAO();
    private ProductDAO productDAO = new ProductDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public void create(Sale s, String customerName) {
        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                var customer = customerDAO.readByName(customerName);
                if (customer == null) {
                    throw new SQLException("Customer not found: " + customerName);
                }
                String sql = "INSERT INTO sales(customer_id, date, total_amount) VALUES(?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, customer.getId());
                    ps.setString(2, s.getDate() == null ? LocalDate.now().toString() : s.getDate().toString());
                    ps.setDouble(3, s.getTotalAmount());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            s.setId(keys.getInt(1));
                        }
                    }
                }

                String getStockSql = "SELECT stock FROM products WHERE id = ?";
                for (SaleDetail d : s.getDetails()) {
                    try (PreparedStatement pss = conn.prepareStatement(getStockSql)) {
                        pss.setInt(1, d.getProductId());
                        try (ResultSet rs = pss.executeQuery()) {
                            if (rs.next()) {
                                int stock = rs.getInt("stock");
                                if (stock < d.getQuantity()) {
                                    throw new SQLException("Insufficient stock for product id=" + d.getProductId());
                                }
                            } else {
                                throw new SQLException("Product not found id=" + d.getProductId());
                            }
                        }
                    }
                }

                String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
                for (SaleDetail d : s.getDetails()) {
                    d.setSaleId(s.getId());
                    detailDAO.createWithConnection(conn, d);
                    try (PreparedStatement psu = conn.prepareStatement(updateStockSql)) {
                        psu.setInt(1, d.getQuantity());
                        psu.setInt(2, d.getProductId());
                        psu.executeUpdate();
                    }
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create sale", e);
        }
    }

    public List<Sale> readAll() {
        List<Sale> list = new ArrayList<>();
        String sql = "SELECT id, customer_id, date, total_amount FROM sales";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Sale s = new Sale();
                s.setId(rs.getInt("id"));
                s.setCustomerId(rs.getInt("customer_id"));
                s.setDate(LocalDate.parse(rs.getString("date")));
                s.setTotalAmount(rs.getDouble("total_amount"));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read sales", e);
        }
        return list;
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
