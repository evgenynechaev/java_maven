package ru.rtk.core;

import ru.rtk.model.*;
import ru.rtk.repository.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Start {
    private final String version = "v.1.0";
    private final State state = new State();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public Start() {
        registerCommands();
        bootstrapApp();
    }

    private void registerCommands() {
        commands.put("about", this::about);
        commands.put("help", this::help);
        commands.put("tables", this::tables);
        commands.put("list", this::list);
        commands.put("info", this::info);
        commands.put("find", this::find);
        commands.put("insert", this::insert);
        commands.put("delete", this::delete);
        commands.put("new", this::orderNew);
        commands.put("cancel", this::orderCancel);
        commands.put("add", this::add);
        commands.put("next", this::orderNextPhase);
        commands.put("bye", this::exit);
        commands.put("exit", this::exit);
        commands.put("quit", this::exit);
    }

    private void about(State state, List<String> args) {
        System.out.println("Программа работы с базой данных " + version);
    }

    private void help(State state, List<String> args) {
        System.out.println("Команды: " + String.join(", ", commands.keySet()));
        System.out.println("""
                about - О программе
                help - Данный текст
                tables - Список таблиц для редактирования и управления
                list <таблица> - Показать все записи таблицы
                info <таблица> <id> - Показать учетную запись с номером id в таблице
                find <таблица> <слово> - Поиск по таблице
                insert <таблица> - Вставить новую запись в таблицу
                delete <таблица> <id> - Удалить запись с номером id из таблицы
                new <id_покупателя> <id_товара> <количество> - Новый заказ
                add products <id_товара> <количество> - Добавить еще несколько экземпляров товара на склад
                cancel <id_заказа> - Отменить заказ, перевести товар на склад, если заказ не 'Завершен'
                next <id_заказа> - Продвижение статуса заказа по сценарию обработки
                bye, exit, quit - Выход из программы
                
                Таблицы:
                customers - Таблица покупателей
                products - Таблица товаров
                orders - Таблица заказов
                status - Служебная таблица статусов заказа
                """
        );
    }

    private void tables(State state, List<String> args) {
        System.out.println("Таблицы: "
                + String.join(", ", state.getTables().stream().toList()));
    }

    private void list(State state, List<String> args) {
        if(args == null || args.isEmpty()) {
            throw new InvalidCommandException("Формат: list <таблица>");
        }

        String table = args.getFirst().toLowerCase();
        switch (table)
        {
            case "customers":
                List<Customer> allCustomers = state.getCustomerRepository().findAll();
                allCustomers.forEach(System.out::println);
                break;
            case "products":
                List<Product> allProducts = state.getProductRepository().findAll();
                allProducts.forEach(System.out::println);
                break;
            case "orders":
                List<Order> allOrders = state.getOrderRepository().findAll();
                allOrders.forEach(order -> {
                    order.setState(state);
                    System.out.println(order);
                });
                break;
            case "status":
                List<Status> allStatuses = state.getOrderStatusRepository().findAll();
                allStatuses.forEach(System.out::println);
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы '" + table + "'");
        }
    }

    private void info(State state, List<String> args) {
        if(args == null || args.size() != 2) {
            throw new InvalidCommandException("Формат: info <таблица> <id>");
        }

        int value;
        String input = args.get(1);
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        String table = args.getFirst().toLowerCase();
        switch (table)
        {
            case "customers":
                List<Customer> customer = state.getCustomerRepository().get(value);
                customer.forEach(System.out::println);
                break;
            case "products":
                List<Product> product = state.getProductRepository().get(value);
                product.forEach(System.out::println);
                break;
            case "orders":
                List<Order> orders = state.getOrderRepository().get(value);
                orders.forEach(order -> {
                    order.setState(state);
                    System.out.println(order);
                });
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы '" + table + "'");
        }
    }

    private void find(State state, List<String> args) {
        if(args == null || args.size() != 2) {
            throw new InvalidCommandException("Формат: find <таблица> <слово>");
        }

        String table = args.getFirst().toLowerCase();
        String value = args.get(1);
        switch (table)
        {
            case "customers":
                List<Customer> customers = state.getCustomerRepository().findByName("%" + value + "%");
                customers.forEach(System.out::println);
                break;
            case "products":
                List<Product> products = state.getProductRepository().findByName("%" + value + "%");
                products.forEach(System.out::println);
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы '" + table + "'");
        }
    }

    private void insert(State state, List<String> args) {
        if(args == null || args.isEmpty()) {
            throw new InvalidCommandException("Формат: insert <таблица>");
        }

        String table = args.getFirst().toLowerCase();
        switch (table)
        {
            case "customers":
                Customer customer = new Customer();
                customer.init();
                if(customer.isValid())
                {
                    state.getCustomerRepository().insert(customer);
                    System.out.println("Покупатель добавлен");
                }
                break;
            case "products":
                Product product = new Product();
                product.init();
                if(product.isValid())
                {
                    state.getProductRepository().insert(product);
                    System.out.println("Товар добавлен");
                }
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы");
        }
    }

    private void delete(State state, List<String> args) {
        if(args == null || args.size() != 2) {
            throw new InvalidCommandException("Формат: delete <таблица> <id>");
        }

        int value;
        String input = args.get(1);
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        String table = args.getFirst().toLowerCase();
        switch (table)
        {
            case "customers":
                try {
                    Customer customer = state.getCustomerRepository().get(value).getFirst();
                    state.getCustomerRepository().delete(value);
                    System.out.println("Удален покупатель " + customer);
                } catch (NoSuchElementException e) {
                    throw new InvalidCommandException("Ошибка удаления.");
                }
                break;
            case "products":
                try {
                    Product product = state.getProductRepository().get(value).getFirst();
                    state.getProductRepository().delete(value);
                    System.out.println("Удален товар " + product);
                } catch (NoSuchElementException e) {
                    throw new InvalidCommandException("Ошибка удаления.");
                }
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы '" + table + "'");
        }
    }

    private void orderNew(State state, List<String> args) {
        if(args == null || args.size() != 3) {
            throw new InvalidCommandException("Формат: new <покупатель_id> <продукт_id> <количество>");
        }

        int customerId;
        String input = "";
        try {
            input = args.getFirst();
            customerId = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        int productId;
        try {
            input = args.get(1);
            productId = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        int quantity;
        try {
            input = args.get(2);
            quantity = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }
        if(quantity < 1) {
            throw new InvalidCommandException("Неправильно введено количество товара '" + quantity + "'");
        }

        int customers = state.getCustomerRepository().get(customerId).size();
        if(customers < 1) {
            throw new InvalidCommandException("Неправильно введен id покупателя '" + customerId + "'");
        }

        List<Product> products = state.getProductRepository().get(productId);
        if(products.isEmpty()) {
            throw new InvalidCommandException("Неправильно введен id товара '" + productId + "'");
        }

        if(quantity > products.getFirst().getQuantity()) {
            throw new InvalidCommandException("Нет столько товара на складе");
        }

        Order order = Order.builder()
                .productId(productId)
                .customerId(customerId)
                .quantity(quantity)
                .statusId(0)
                .build();
        state.getOrderRepository().insert(order);
        state.getProductRepository().subtract(productId, quantity);
        System.out.println("Заказ добавлен");
    }

    private void add(State state, List<String> args) {
        if (args == null || args.size() != 4) {
            throw new InvalidCommandException("Формат: add <таблица> <id> <количество>");
        }

        int id, quantity;
        String input = "";
        try {
            input = args.get(1);
            id = Integer.parseInt(input);
            input = args.get(2);
            quantity = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        String table = args.getFirst().toLowerCase();
        switch (table)
        {
            case "products":
                try {
                    Product product = state.getProductRepository().get(id).getFirst();
                    state.getProductRepository().add(id, quantity);
                    System.out.println("Добавлен товар " + product);
                } catch (NoSuchElementException e) {
                    throw new InvalidCommandException("Ошибка добавления товара.");
                }
                break;
            default:
                throw new InvalidCommandException("Нет такой таблицы '" + table + "'");
        }
    }

    private void orderCancel(State state, List<String> args) {
        if (args == null || args.isEmpty()) {
            throw new InvalidCommandException("Формат: next <заказ_id>");
        }

        int orderId;
        String input = "";
        try {
            input = args.getFirst();
            orderId = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        List<Order> orders = state.getOrderRepository().get(orderId);
        if(orders.isEmpty()) {
            throw new InvalidCommandException("Неправильно введен id заказа '" + orderId + "'");
        }

        Order order = orders.getFirst();
        if(order.getStatusId() >= 3) {
            throw new InvalidCommandException("Заказ значится как завершенный. Возврат на склад невозможен.");
        }
        state.getOrderRepository().delete(order.getId());
        state.getProductRepository().add(order.getProductId(), order.getQuantity());
        System.out.println("Заказ " + order.getId() + " отменен.");
    }


    private void orderNextPhase(State state, List<String> args) {
        if(args == null || args.isEmpty()) {
            throw new InvalidCommandException("Формат: next <заказ_id>");
        }

        int orderId;
        String input = "";
        try {
            input = args.getFirst();
            orderId = Integer.parseInt(input);
        } catch (NumberFormatException | NullPointerException nfe) {
            throw new InvalidCommandException("Неправильно введено значение '" + input + "'");
        }

        List<Order> orders = state.getOrderRepository().get(orderId);
        if(orders.isEmpty()) {
            throw new InvalidCommandException("Неправильно введен id заказа '" + orderId + "'");
        }

        Order order = orders.getFirst();
        if(order.getStatusId() >= 3) {
            throw new InvalidCommandException("Заказ значится как доставленный. Это максимальный статус заказа.");
        }

        state.getOrderRepository().nextPhase(order.getId());
        Order updated = state.getOrderRepository().get(orderId).getFirst();
        updated.setState(state);
        System.out.println(updated);
    }

    private void exit(State ctx, List<String> a) {
        throw new EndAppException("\nРабота программы завершена.");
    }

    private void bootstrapApp() {
        try {
            state.init();
        } catch (InvalidCommandException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (EndAppException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка: "
                    + e.getClass().getSimpleName()
                    + ": " + e.getMessage());
        }
        System.out.println();
    }

    public void run() {
        String CHARSET = "UTF-8";
        // String CHARSET = "CP866";

        System.out.println("Приложение учета заказов. 'help' - команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in, CHARSET))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\\s+"));
                if(parts.isEmpty()) {
                    continue;
                }
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (EndAppException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Нажмите [Ввод]");
                    in.readLine();
                    System.exit(0);
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: "
                            + e.getClass().getSimpleName()
                            + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
