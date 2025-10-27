package easystore.dao;

import easystore.model.Supplier;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO implements GenericDAO<Supplier> {

    @Override
    public void create(Supplier s) {
        String sql = "INSERT INTO suppliers(name, last_name, phone_number, dni) VALUES(?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getPhoneNumber());
            ps.setString(4, s.getDni());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create supplier", e);
        }
    }

    @Override
    public Supplier readByName(String name) {
        String sql = "SELECT id, name, last_name, phone_number, dni FROM suppliers WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Supplier s = new Supplier();
                    s.setId(rs.getInt("id"));
                    s.setName(rs.getString("name"));
                    s.setLastName(rs.getString("last_name"));
                    s.setPhoneNumber(rs.getString("phone_number"));
                    s.setDni(rs.getString("dni"));
                    return s;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read supplier", e);
        }
        return null;
    }

    @Override
    public List<Supplier> readAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT id, name, last_name, phone_number, dni FROM suppliers";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setLastName(rs.getString("last_name"));
                s.setPhoneNumber(rs.getString("phone_number"));
                s.setDni(rs.getString("dni"));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read suppliers", e);
        }
        return list;
    }

    @Override
    public void update(Supplier s) {
        String sql = "UPDATE suppliers SET name=?, last_name=?, phone_number=?, dni=? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getPhoneNumber());
            ps.setString(4, s.getDni());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update supplier", e);
        }
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM suppliers WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete supplier", e);
        }
    }
}
