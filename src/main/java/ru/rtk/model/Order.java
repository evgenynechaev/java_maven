package ru.rtk.model;

import lombok.*;
import ru.rtk.core.State;
import ru.rtk.core.Utils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int id = -1;
    private int productId = -1;
    private int customerId = -1;
    private Timestamp saleDate = new Timestamp(0);
    private int quantity = 0;
    private int statusId = -1;
    private State state = null;

    @Override
    public String toString() {
        if(id < 0) {
            return "Пустой элемент";
        }

        if(state == null) {
            return String.format(
                    "(id=%d от %s) [%d] Покупатель: %d, Товар: %d, Количество: %d",
                    this.getId(),
                    this.getSaleDate().toLocalDateTime().format(Utils.dateAndTime()),
                    this.getStatusId(),
                    this.getCustomerId(),
                    this.getProductId(),
                    this.getQuantity());
        }

        Customer customer = state.getCustomerRepository().get(this.getCustomerId()).getFirst();
        Product product = state.getProductRepository().get(this.getProductId()).getFirst();
        Status status = state.getOrderStatusRepository().get(this.getStatusId()).getFirst();

        return String.format(
                "(id=%d от %s) [%s] Покупатель: %s, Товар: %s, Количество: %d",
                this.getId(),
                this.getSaleDate().toLocalDateTime().format(Utils.dateAndTime()),
                status.getName(),
                customer.getLastName() + " " + customer.getFirstName(),
                product.getName(),
                this.getQuantity());
    }

    public boolean isValid() {
        return this.getId() >= 0
                && this.getCustomerId() >= 0
                && this.getProductId() >= 0
                && this.saleDate.after(new Timestamp(0))
                && this.getQuantity() > 0
                && this.getStatusId() >= 0;
    }

    public void newOrder() {
        System.out.println("Новый заказ");
        // this.name = Utils.inputString("Введите наименование: ", 5, false);
        // this.price = Utils.inputDouble("Введите цену: ", 0.0);
        // this.quantity = Utils.inputInt("Введите количество: ", 1);
        // this.category = Utils.inputString("Введите категорию: ", 3, false);
    }
}
