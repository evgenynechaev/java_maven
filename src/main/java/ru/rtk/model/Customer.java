package ru.rtk.model;

import lombok.*;
import ru.rtk.core.Utils;

import java.util.Scanner;

// @Value
// @Data = @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    // @Builder.Default private long created = System.currentTimeMillis();

    @Override
    public String toString() {
        return String.format(
                "(id=%d) %s %s, тел.%s, email:%s",
                this.getId(),
                this.getLastName(),
                this.getFirstName(),
                this.getPhone(),
                this.getEmail());
    }

    public void init() {
        System.out.println("Новый покупатель");
        this.lastName = Utils.inputString("Введите фамилию: ", 2, false);
        this.firstName = Utils.inputString("Введите имя: ", 2, false);
        this.phone = Utils.inputString("Введите телефон: ", 0, true);
        this.email = Utils.inputString("Введите эл.почту: ", 0, true);
    }

    public boolean isValid() {
        return !this.firstName.isEmpty() && !this.lastName.isEmpty();
    }
}
