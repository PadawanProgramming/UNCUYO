package easystore.controller;

import easystore.dao.SupplierDAO;
import easystore.model.Supplier;
import easystore.exceptions.NotFoundException;

public class SupplierController {

    private final SupplierDAO supplierDAO;

    public SupplierController(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    public void createSupplier(Supplier s) {
        supplierDAO.create(s);
    }

    public Supplier getByName(String name) throws NotFoundException {
        Supplier s = supplierDAO.readByName(name);
        if (s == null) {
            throw new NotFoundException("Supplier not found: " + name);
        }
        return s;
    }

    public void updateSupplier(Supplier s) {
        supplierDAO.update(s);
    }

    public void deleteByName(String name) {
        supplierDAO.deleteByName(name);
    }
}
