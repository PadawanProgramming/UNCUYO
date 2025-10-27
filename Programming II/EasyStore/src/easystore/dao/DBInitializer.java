package easystore.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {
        try (Connection conn = ConnectionManager.getConnection(); Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");

            st.execute("CREATE TABLE IF NOT EXISTS categories ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT UNIQUE NOT NULL, "
                    + "description TEXT"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS products ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL UNIQUE, "
                    + "description TEXT, "
                    + "sales_price REAL NOT NULL, "
                    + "purchase_price REAL NOT NULL, "
                    + "stock INTEGER NOT NULL DEFAULT 0, "
                    + "category_id INTEGER, "
                    + "FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS customers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL UNIQUE, "
                    + "last_name TEXT NOT NULL, "
                    + "phone_number TEXT, "
                    + "dni TEXT UNIQUE"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS suppliers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL UNIQUE, "
                    + "last_name TEXT NOT NULL, "
                    + "phone_number TEXT, "
                    + "dni TEXT UNIQUE"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS purchases ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "supplier_id INTEGER NOT NULL, "
                    + "date TEXT, "
                    + "total_amount REAL NOT NULL, "
                    + "FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS purchase_details ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "purchase_id INTEGER NOT NULL, "
                    + "product_id INTEGER NOT NULL, "
                    + "quantity INTEGER NOT NULL, "
                    + "unit_price REAL NOT NULL, "
                    + "total REAL NOT NULL, "
                    + "FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (product_id) REFERENCES products(id)"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS sales ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "customer_id INTEGER NOT NULL, "
                    + "date TEXT, "
                    + "total_amount REAL NOT NULL, "
                    + "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS sale_details ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "sale_id INTEGER NOT NULL, "
                    + "product_id INTEGER NOT NULL, "
                    + "quantity INTEGER NOT NULL, "
                    + "unit_price REAL NOT NULL, "
                    + "total REAL NOT NULL, "
                    + "FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (product_id) REFERENCES products(id)"
                    + ");");

            System.out.println("Database initialized.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DB", e);
        }
    }
}
