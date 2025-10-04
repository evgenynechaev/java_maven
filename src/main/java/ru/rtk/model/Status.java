package ru.rtk.model;

import lombok.*;
import ru.rtk.core.Utils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private int id;
    private String name;

    public void init() {
        System.out.println("Новый статус");
        this.name = Utils.inputString("Введите название: ", 4, false);
    }

    public boolean isValid() {
        return !this.name.isEmpty();
    }
}
