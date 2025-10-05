package ru.rtk.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.rtk.App;
import ru.rtk.repository.*;

import javax.sql.DataSource;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Getter
@NoArgsConstructor
public class State {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private OrderStatusRepository orderStatusRepository;
    private final List<String> tables = new ArrayList<>();

    public void init() {
        String db_properties = "application.properties";
        try(InputStream input = App.class
                .getClassLoader()
                .getResourceAsStream(db_properties)
        ) {
            Properties props = new Properties();
            props.load(input);

            // String url = "jdbc:postgresql://localhost:5431/market";
            // String user = "postgres";
            // String password = "12345";
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            if(url.isEmpty() || user.isEmpty() || password.isEmpty()) {
                throw new EndAppException(
                        "В файле конфигурации БД '" + db_properties + "' нет необходимых параметров\n"
                );
            }

            DataSource dataSource = new DriverManagerDataSource(url, user, password);

            this.productRepository = new ProductRepositoryImpl(dataSource);
            this.customerRepository = new CustomerRepositoryImpl(dataSource);
            this.orderRepository = new OrderRepositoryImpl(dataSource);
            this.orderStatusRepository = new OrderStatusRepositoryImpl(dataSource);

            this.tables.addAll(List.of("customers", "products", "orders"));
        } catch (NullPointerException | IOException e) {
            throw new EndAppException(
                    "Не удалось загрузить файл конфигурации БД '"
                            + db_properties
                            + "'.\nЗавершение работы программы.\n"
            );
        }
    }
}
