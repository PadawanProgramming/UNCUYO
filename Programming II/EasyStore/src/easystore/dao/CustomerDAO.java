package easystore.dao;

import easystore.model.Customer;
import easystore.exceptions.DataAccessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO implements GenericDAO<Customer> {

    @Override
    public void create(Customer c) {
        String sql = "INSERT INTO customers(name, last_name, phone_number, dni) VALUES(?,?,?,?)";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getDni());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create customer", e);
        }
    }

    @Override
    public Customer readByName(String name) {
        String sql = "SELECT id, name, last_name, phone_number, dni FROM customers WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setLastName(rs.getString("last_name"));
                    c.setPhoneNumber(rs.getString("phone_number"));
                    c.setDni(rs.getString("dni"));
                    return c;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read customer", e);
        }
        return null;
    }

    @Override
    public List<Customer> readAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT id, name, last_name, phone_number, dni FROM customers";
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setLastName(rs.getString("last_name"));
                c.setPhoneNumber(rs.getString("phone_number"));
                c.setDni(rs.getString("dni"));
                list.add(c);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to read customers", e);
        }
        return list;
    }

    @Override
    public void update(Customer c) {
        String sql = "UPDATE customers SET name=?, last_name=?, phone_number=?, dni=? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getDni());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update customer", e);
        }
    }

    @Override
    public void deleteByName(String name) {
        String sql = "DELETE FROM customers WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete customer", e);
        }
    }
}
