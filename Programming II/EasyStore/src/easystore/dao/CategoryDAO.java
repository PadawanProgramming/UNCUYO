package easystore.dao;

import easystore.model.Category;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements GenericDAO<Category> {
  
    @Override
    public void create(Category c) {
        String sql = "INSERT INTO categories(name, description) VALUES(?,?)";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create category", e);
        }
    }

    @Override
    public Category readByName(String name) {
        String sql = "SELECT id, name, description FROM categories WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setDescription(rs.getString("description"));
                    return c;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read category", e);
        }
        return null;
    }

    @Override
    public List<Category> readAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name, description FROM categories";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read categories", e);
        }
        return list;
    }

    @Override
    public void update(Category c) {
        String sql = "UPDATE categories SET name=?, description=? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update category", e);
        }
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM categories WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete category", e);
        }
    }
}
