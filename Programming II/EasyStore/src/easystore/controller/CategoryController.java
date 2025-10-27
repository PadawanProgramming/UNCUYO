package easystore.controller;

import easystore.dao.CategoryDAO;
import easystore.model.Category;
import easystore.exceptions.NotFoundException;

public class CategoryController {

    private final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void createCategory(Category c) {
        categoryDAO.create(c);
    }

    public Category getByName(String name) throws NotFoundException {
        Category c = categoryDAO.readByName(name);
        if (c == null) {
            throw new NotFoundException("Category not found: " + name);
        }
        return c;
    }

    public void updateCategory(Category c) {
        categoryDAO.update(c);
    }

    public void deleteByName(String name) {
        categoryDAO.deleteByName(name);
    }
}
