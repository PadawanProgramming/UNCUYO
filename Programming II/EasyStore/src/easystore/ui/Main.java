package easystore.ui;

import easystore.controller.*;
import easystore.dao.*;
import easystore.model.*;
import easystore.service.*;
import easystore.dao.DBInitializer;
import easystore.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DBInitializer.initialize();

        // DAOs
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        PurchaseDAO purchaseDAO = new PurchaseDAO();
        SaleDAO saleDAO = new SaleDAO();

        // Controllers
        ProductController productController = new ProductController(productDAO);
        CategoryController categoryController = new CategoryController(categoryDAO);
        CustomerController customerController = new CustomerController(customerDAO);
        SupplierController supplierController = new SupplierController(supplierDAO);
        PurchaseController purchaseController = new PurchaseController(purchaseDAO);
        SaleController saleController = new SaleController(saleDAO);

        // Services
        InventoryService inventory = new InventoryService(productDAO);
        FinancesService finances = new FinancesService(purchaseDAO, saleDAO);

        boolean running = true;
        while (running) {
            printMenu();
            String opt = scanner.nextLine().trim();
            try {
                switch (opt) {
                    // --- Products ---
                    case "1" ->
                        createProduct(productController);
                    case "2" ->
                        listProducts(inventory);
                    case "3" ->
                        updateProduct(productController);
                    case "4" ->
                        deleteProduct(productController);
                    // --- Customers ---
                    case "5" ->
                        createCustomer(customerController);
                    case "6" ->
                        listCustomers(customerDAO);
                    case "7" ->
                        updateCustomer(customerController);
                    case "8" ->
                        deleteCustomer(customerController);
                    // --- Providers ---
                    case "9" ->
                        createSupplier(supplierController);
                    case "10" ->
                        listSuppliers(supplierDAO);
                    case "11" ->
                        updateSupplier(supplierController);
                    case "12" ->
                        deleteSupplier(supplierController);
                    // --- Categorires ---
                    case "13" ->
                        createCategory(categoryController);
                    case "14" ->
                        listCategories(categoryDAO);
                    case "15" ->
                        updateCategory(categoryController);
                    case "16" ->
                        deleteCategory(categoryController);
                    // --- Transactions ---
                    case "17" ->
                        createPurchase(purchaseController, productDAO);
                    case "18" ->
                        listPurchases(purchaseController);
                    case "19" ->
                        deletePurchase(purchaseController);
                    case "20" ->
                        createSale(saleController, productDAO);
                    case "21" ->
                        listSales(saleController);
                    case "22" ->
                        deleteSale(saleController);
                    // --- Reports ---
                    case "23" ->
                        finances.generateReport();
                    case "24" ->
                        inventory.generateReport();
                    // --- Exit ---
                    case "0" -> {
                        running = false;
                        System.out.println("¡Adiós!");
                    }
                    default ->
                        System.out.println("Opción inválida. Intente de nuevo");
                }
            } catch (NotFoundException nf) {
                System.out.println("No encontrado: " + nf.getMessage());
            } catch (NumberFormatException nfe) {
                System.out.println("Error de entrada: Por favor, ingrese un número válido");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
        try {
            ConnectionManager.closeConnection();
        } catch (Exception ex) {
            System.out.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n--- EasyStore - Sistema de Gestión ---");

        System.out.println("\n--- 🛒 Gestión de Productos ---");
        System.out.println("1) Crear producto");
        System.out.println("2) Listar productos (Reporte de Inventario)");
        System.out.println("3) Actualizar producto (por nombre)");
        System.out.println("4) Eliminar producto (por nombre)");

        System.out.println("\n--- 🧑 Gestión de Clientes ---");
        System.out.println("5) Crear cliente");
        System.out.println("6) Listar clientes");
        System.out.println("7) Actualizar cliente (por nombre)");
        System.out.println("8) Eliminar cliente (por nombre)");

        System.out.println("\n--- 📦 Gestión de Proveedores ---");
        System.out.println("9) Crear proveedor");
        System.out.println("10) Listar proveedores");
        System.out.println("11) Actualizar proveedor (por nombre)");
        System.out.println("12) Eliminar proveedor (por nombre)");

        System.out.println("\n--- 🏷️ Gestión de Categorías ---");
        System.out.println("13) Crear categoría");
        System.out.println("14) Listar categorías");
        System.out.println("15) Actualizar categoría (por nombre)");
        System.out.println("16) Eliminar categoría (por nombre)");

        System.out.println("\n--- 💰 Gestión de Transacciones ---");
        System.out.println("17) Registrar Compra (por nombre de proveedor)");
        System.out.println("18) Listar Compras");
        System.out.println("19) Eliminar Compra (por ID)");
        System.out.println("20) Registrar Venta (por nombre de cliente)");
        System.out.println("21) Listar Ventas");
        System.out.println("22) Eliminar Venta (por ID)");

        System.out.println("\n--- 📊 Reportes ---");
        System.out.println("23) Reporte Financiero");
        System.out.println("24) Reporte de Inventario");

        System.out.println("\n0) Salir");
        System.out.print("Ingrese una opción: ");
    }

    private static void createProduct(ProductController controller) {
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Precio de compra: ");
        double pp = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Precio de venta: ");
        double sp = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock inicial: ");
        int st = Integer.parseInt(scanner.nextLine().trim());
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPurchasePrice(pp);
        p.setSalesPrice(sp);
        p.setStock(st);
        System.out.print("Nombre de la categoría (para omitir presione Enter): ");
        String catName = scanner.nextLine().trim();
        if (!catName.isEmpty()) {
            var cat = new easystore.dao.CategoryDAO().readByName(catName);
            if (cat == null) {
                System.out.println("Categoría no encontrada, creando nueva categoría con ese nombre.");
                var newCat = new easystore.model.Category();
                newCat.setName(catName);
                newCat.setDescription("");
                new easystore.dao.CategoryDAO().create(newCat);
                cat = new easystore.dao.CategoryDAO().readByName(catName);
            }
            if (cat != null) {
                p.setCategoryId(cat.getId());
            }
        }
        controller.createProduct(p);
        System.out.println("Producto creado exitosamente.");
    }

    private static void listProducts(InventoryService inventory) {
        inventory.generateReport();
    }

    private static void updateProduct(ProductController controller) throws NotFoundException {
        System.out.print("Ingrese el nombre del producto a actualizar: ");
        String name = scanner.nextLine().trim();
        Product p = controller.getByName(name);
        System.out.print("Nuevo nombre (" + p.getName() + "): ");
        String n = scanner.nextLine().trim();
        if (!n.isEmpty()) {
            p.setName(n);
        }
        System.out.print("Nueva descripción (" + p.getDescription() + "): ");
        String d = scanner.nextLine().trim();
        if (!d.isEmpty()) {
            p.setDescription(d);
        }
        System.out.print("Nuevo precio de venta (" + p.getSalesPrice() + "): ");
        String sp = scanner.nextLine().trim();
        if (!sp.isEmpty()) {
            p.setSalesPrice(Double.parseDouble(sp));
        }
        System.out.print("Nuevo stock (" + p.getStock() + "): ");
        String ss = scanner.nextLine().trim();
        if (!ss.isEmpty()) {
            p.setStock(Integer.parseInt(ss));
        }
        controller.updateProduct(p);
        System.out.println("Producto actualizado exitosamente.");
    }

    private static void deleteProduct(ProductController controller) {
        System.out.print("Ingrese el nombre del producto a eliminar: ");
        String name = scanner.nextLine().trim();
        controller.deleteByName(name);
        System.out.println("Producto eliminado");
    }

    private static void createCustomer(CustomerController controller) {
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Apellido: ");
        String ln = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String ph = scanner.nextLine().trim();
        System.out.print("DNI: ");
        String dni = scanner.nextLine().trim();
        var c = new Customer();
        c.setName(name);
        c.setLastName(ln);
        c.setPhoneNumber(ph);
        c.setDni(dni);
        controller.createCustomer(c);
        System.out.println("Cliente creado exitosamente");
    }

    private static void listCustomers(CustomerDAO dao) {
        System.out.println("--- Clientes Registrados ---");
        for (var c : dao.readAll()) {
            System.out.println("ID: " + c.getId() + " | Nombre: " + c.getName() + " " + c.getLastName() + " | DNI: " + c.getDni());
        }
    }

    private static void updateCustomer(CustomerController controller) throws NotFoundException {
        System.out.print("Ingrese el nombre del cliente a actualizar: ");
        String name = scanner.nextLine().trim();
        var c = controller.getByName(name);
        System.out.print("Nuevo nombre (" + c.getName() + "): ");
        String n = scanner.nextLine().trim();
        if (!n.isEmpty()) {
            c.setName(n);
        }
        System.out.print("Nuevo apellido (" + c.getLastName() + "): ");
        String ln = scanner.nextLine().trim();
        if (!ln.isEmpty()) {
            c.setLastName(ln);
        }
        System.out.print("Nuevo teléfono (" + c.getPhoneNumber() + "): ");
        String ph = scanner.nextLine().trim();
        if (!ph.isEmpty()) {
            c.setPhoneNumber(ph);
        }
        System.out.print("Nuevo DNI (" + c.getDni() + "): ");
        String dni = scanner.nextLine().trim();
        if (!dni.isEmpty()) {
            c.setDni(dni);
        }
        controller.updateCustomer(c);
        System.out.println("Cliente actualizado exitosamente");
    }

    private static void deleteCustomer(CustomerController controller) {
        System.out.print("Ingrese el nombre del cliente a eliminar: ");
        String name = scanner.nextLine().trim();
        controller.deleteByName(name);
        System.out.println("Cliente eliminado");
    }

    private static void createSupplier(SupplierController controller) {
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Apellido: ");
        String ln = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String ph = scanner.nextLine().trim();
        System.out.print("DNI: ");
        String dni = scanner.nextLine().trim();
        var s = new easystore.model.Supplier();
        s.setName(name);
        s.setLastName(ln);
        s.setPhoneNumber(ph);
        s.setDni(dni);
        controller.createSupplier(s);
        System.out.println("Proveedor creado exitosamente");
    }

    private static void listSuppliers(SupplierDAO dao) {
        System.out.println("--- Proveedores Registrados ---");
        for (var s : dao.readAll()) {
            System.out.println("ID: " + s.getId() + " | Nombre: " + s.getName() + " " + s.getLastName() + " | DNI: " + s.getDni());
        }
    }

    private static void updateSupplier(SupplierController controller) throws NotFoundException {
        System.out.print("Ingrese el nombre del proveedor a actualizar: ");
        String name = scanner.nextLine().trim();
        var s = controller.getByName(name);
        System.out.print("Nuevo nombre (" + s.getName() + "): ");
        String n = scanner.nextLine().trim();
        if (!n.isEmpty()) {
            s.setName(n);
        }
        System.out.print("Nuevo apellido (" + s.getLastName() + "): ");
        String ln = scanner.nextLine().trim();
        if (!ln.isEmpty()) {
            s.setLastName(ln);
        }
        System.out.print("Nuevo teléfono (" + s.getPhoneNumber() + "): ");
        String ph = scanner.nextLine().trim();
        if (!ph.isEmpty()) {
            s.setPhoneNumber(ph);
        }
        System.out.print("Nuevo DNI (" + s.getDni() + "): ");
        String dni = scanner.nextLine().trim();
        if (!dni.isEmpty()) {
            s.setDni(dni);
        }
        controller.updateSupplier(s);
        System.out.println("Proveedor actualizado exitosamente");
    }

    private static void deleteSupplier(SupplierController controller) {
        System.out.print("Ingrese el nombre del proveedor a eliminar: ");
        String name = scanner.nextLine().trim();
        controller.deleteByName(name);
        System.out.println("Proveedor eliminado (si existía).");
    }

    private static void createCategory(CategoryController controller) {
        System.out.print("Nombre de la categoría: ");
        String name = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String desc = scanner.nextLine().trim();
        var c = new easystore.model.Category();
        c.setName(name);
        c.setDescription(desc);
        controller.createCategory(c);
        System.out.println("Categoría creada exitosamente");
    }

    private static void listCategories(CategoryDAO dao) {
        System.out.println("--- Categorías Registradas ---");
        for (var c : dao.readAll()) {
            System.out.println("ID: " + c.getId() + " | Nombre: " + c.getName() + " - Descripción: " + c.getDescription());
        }
    }

    private static void updateCategory(CategoryController controller) throws NotFoundException {
        System.out.print("Ingrese el nombre de la categoría a actualizar: ");
        String name = scanner.nextLine().trim();
        var c = controller.getByName(name);
        System.out.print("Nueva descripción (" + c.getDescription() + "): ");
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) {
            c.setDescription(desc);
        }
        controller.updateCategory(c);
        System.out.println("Categoría actualizada exitosamente");
    }

    private static void deleteCategory(CategoryController controller) {
        System.out.print("Ingrese el nombre de la categoría a eliminar: ");
        String name = scanner.nextLine().trim();
        controller.deleteByName(name);
        System.out.println("Categoría eliminada");
    }

    private static void createPurchase(PurchaseController controller, ProductDAO productDAO) throws NotFoundException {
        System.out.print("Nombre del proveedor: ");
        String supplierName = scanner.nextLine().trim();
        var purchase = new Purchase();
        purchase.setDate(LocalDate.now());
        double total = 0.0;
        System.out.println("--- Ingrese los productos (escriba listo para terminar) ---");
        while (true) {
            System.out.print("Nombre del producto: ");
            String pname = scanner.nextLine().trim();
            if (pname.equalsIgnoreCase("listo")) {
                break;
            }
            var prod = productDAO.readByName(pname);
            if (prod == null) {
                System.out.println("Producto no encontrado. Intente de nuevo");
                continue;
            }
            System.out.print("Cantidad: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Precio unitario de compra: ");
            double up = Double.parseDouble(scanner.nextLine().trim());
            var d = new PurchaseDetail();
            d.setProductId(prod.getId());
            d.setQuantity(qty);
            d.setUnitPrice(up);
            d.setTotal(d.calculateAmount());
            purchase.addDetail(d);
            total += d.getTotal();
        }
        purchase.setTotalAmount(total);
        controller.createPurchase(purchase, supplierName);
        System.out.println("Compra registrada exitosamente");
    }

    private static void listPurchases(PurchaseController controller) {
        System.out.println("--- Compras Registradas ---");
        for (easystore.model.Purchase p : controller.listPurchases()) {
            System.out.printf("ID=%d | Proveedor ID=%d | Fecha=%s | Total=%.2f%n", p.getId(), p.getSupplierId(), p.getDate(), p.getTotalAmount());
            System.out.println("  - Detalles de Compra:");
            for (easystore.model.PurchaseDetail d : p.getDetails()) {
                System.out.printf("    - Prod. ID=%d | Cantidad=%d | P. Unitario=%.2f | Total=%.2f%n", d.getProductId(), d.getQuantity(), d.getUnitPrice(), d.getTotal());
            }
        }
    }

    private static void deletePurchase(PurchaseController controller) {
        System.out.print("Ingrese el ID de la compra a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        controller.deleteById(id);
        System.out.println("Compra eliminada exitosamente");
    }

    private static void createSale(SaleController controller, ProductDAO productDAO) throws NotFoundException {
        System.out.print("Nombre del cliente: ");
        String customerName = scanner.nextLine().trim();
        var sale = new Sale();
        sale.setDate(LocalDate.now());
        double total = 0.0;
        System.out.println("--- Ingrese los productos (escriba 'listo' para terminar) ---");
        while (true) {
            System.out.print("Nombre del producto: ");
            String pname = scanner.nextLine().trim();
            if (pname.equalsIgnoreCase("listo")) {
                break;
            }
            var prod = productDAO.readByName(pname);
            if (prod == null) {
                System.out.println("Producto no encontrado. Intente de nuevo");
                continue;
            }
            System.out.print("Cantidad: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty > prod.getStock()) {
                System.out.println("Advertencia: Stock insuficiente. Stock actual: " + prod.getStock());
                continue;
            }

            var d = new SaleDetail();
            d.setProductId(prod.getId());
            d.setQuantity(qty);
            d.setUnitPrice(prod.getSalesPrice());
            d.setTotal(d.calculateAmount());
            sale.addDetail(d);
            total += d.getTotal();
        }
        sale.setTotalAmount(total);
        controller.createSale(sale, customerName);
        System.out.println("Venta registrada exitosamente");
    }

    private static void listSales(SaleController controller) {
        System.out.println("--- Ventas Registradas ---");
        for (easystore.model.Sale s : controller.listSales()) {
            System.out.printf("ID=%d | Cliente ID=%d | Fecha=%s | Total=%.2f%n", s.getId(), s.getCustomerId(), s.getDate(), s.getTotalAmount());
            System.out.println("  - Detalles de Venta:");
            for (easystore.model.SaleDetail d : s.getDetails()) {
                System.out.printf("    - Prod. ID=%d | Cantidad=%d | P. Unitario=%.2f | Total=%.2f%n", d.getProductId(), d.getQuantity(), d.getUnitPrice(), d.getTotal());
            }
        }
    }

    private static void deleteSale(SaleController controller) {
        System.out.print("Ingrese el ID de la venta a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        controller.deleteById(id);
        System.out.println("Venta eliminada exitosamente");
    }

}
