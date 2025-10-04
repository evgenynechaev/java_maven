package ru.rtk.model;

import lombok.*;
import ru.rtk.core.Utils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String category;


    @Override
    public String toString() {
        return String.format("(id=%d) %s (%s), цена %.2f, кол. %d",
                this.getId(),
                this.getName(),
                this.getCategory(),
                this.getPrice(),
                this.getQuantity());
    }

    public void init() {
        System.out.println("Новый товар");
        this.name = Utils.inputString("Введите наименование: ", 5, false);
        this.price = Utils.inputDouble("Введите цену: ", 0.0);
        this.quantity = Utils.inputInt("Введите количество: ", 1);
        this.category = Utils.inputString("Введите категорию: ", 3, false);
    }

    public boolean isValid() {
        return !this.name.isEmpty() && !this.category.isEmpty();
    }
}
