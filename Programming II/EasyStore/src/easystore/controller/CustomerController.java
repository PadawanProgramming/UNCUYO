package easystore.controller;

import easystore.dao.CustomerDAO;
import easystore.model.Customer;
import easystore.exceptions.NotFoundException;

public class CustomerController {

    private final CustomerDAO customerDAO;

    public CustomerController(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void createCustomer(Customer c) {
        customerDAO.create(c);
    }

    public Customer getByName(String name) throws NotFoundException {
        Customer c = customerDAO.readByName(name);
        if (c == null) {
            throw new NotFoundException("Customer not found: " + name);
        }
        return c;
    }

    public void updateCustomer(Customer c) {
        customerDAO.update(c);
    }

    public void deleteByName(String name) {
        customerDAO.deleteByName(name);
    }
}
