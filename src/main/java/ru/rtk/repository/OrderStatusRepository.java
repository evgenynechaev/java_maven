package ru.rtk.repository;

import ru.rtk.model.Status;

import java.util.List;

public interface OrderStatusRepository {
    List<Status> get(int id);
    List<Status> findAll();
    List<Status> findByName(String name);
}
