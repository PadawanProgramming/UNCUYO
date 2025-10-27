package easystore.dao;

import java.util.List;

public interface GenericDAO<T> {

    void create(T t);

    T readByName(String name);

    List<T> readAll();

    void update(T t);

    void deleteByName(String name);
}
