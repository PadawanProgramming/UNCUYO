package easystore.dao;

import easystore.model.Product;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements GenericDAO<Product> {

    @Override
    public void create(Product p) {
        String sql = "INSERT INTO products(name, description, sales_price, purchase_price, stock, category_id) VALUES(?,?,?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getSalesPrice());
            ps.setDouble(4, p.getPurchasePrice());
            ps.setInt(5, p.getStock());
            if (p.getCategoryId() != null) {
                ps.setInt(6, p.getCategoryId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create product", e);
        }
    }

    @Override
    public Product readByName(String name) {
        String sql = "SELECT id, name, description, sales_price, purchase_price, stock, category_id FROM products WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setSalesPrice(rs.getDouble("sales_price"));
                    p.setPurchasePrice(rs.getDouble("purchase_price"));
                    p.setStock(rs.getInt("stock"));
                    int cid = rs.getInt("category_id");
                    if (!rs.wasNull()) {
                        p.setCategoryId(cid);
                    }
                    return p;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read product by name", e);
        }
        return null;
    }

    @Override
    public List<Product> readAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, description, sales_price, purchase_price, stock, category_id FROM products";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setSalesPrice(rs.getDouble("sales_price"));
                p.setPurchasePrice(rs.getDouble("purchase_price"));
                p.setStock(rs.getInt("stock"));
                int cid = rs.getInt("category_id");
                if (!rs.wasNull()) {
                    p.setCategoryId(cid);
                }
                list.add(p);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read products", e);
        }
        return list;
    }

    @Override
    public void update(Product p) {
        String sql = "UPDATE products SET name=?, description=?, sales_price=?, purchase_price=?, stock=?, category_id=? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getSalesPrice());
            ps.setDouble(4, p.getPurchasePrice());
            ps.setInt(5, p.getStock());
            if (p.getCategoryId() != null) {
                ps.setInt(6, p.getCategoryId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update product", e);
        }
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM products WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete product", e);
        }
    }

    // helper to update stock by product name
    public void changeStockByName(String name, int delta) {
        String sql = "UPDATE products SET stock = stock + ? WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to change stock", e);
        }
    }
}
