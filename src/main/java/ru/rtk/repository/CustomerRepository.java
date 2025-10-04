package ru.rtk.repository;

import ru.rtk.model.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> get(int id);
    List<Customer> findAll();
    List<Customer> findByName(String name);
    void insert(Customer customer);
    void delete(int id);
}
