package ru.rtk.repository;

import ru.rtk.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> get(int id);
    List<Product> findAll();
    List<Product> findByName(String name);
    void insert(Product product);
    void delete(int id);
    void add(int id, int quantity);
    void subtract(int id, int quantity);
}
