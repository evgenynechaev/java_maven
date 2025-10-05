package ru.rtk.repository;

import ru.rtk.model.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> get(int id);
    List<Order> findAll();
    // List<Order> findByFirstName(String firstName);
    // List<Order> findByLastName(String lastName);
    void insert(Order customer);
    void delete(int id);
    void add(int id, int quantity);
    void nextPhase(int id);
}
