package ru.rtk.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rtk.model.Order;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    //language=SQL
    private static final String SQL_INSERT =
            "INSERT INTO orders(product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?);";

    //language=SQL
    private static final String SQL_DELETE =
            "DELETE FROM orders WHERE id=?;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM orders WHERE id=?;";

    //language=SQL
    private static final String SQL_SELECT_ALL =
            "SELECT * FROM orders ORDER BY id;";

    //language=SQL
    private static final String SQL_NEXT_PHASE =
            "UPDATE orders SET status_id=status_id+1 WHERE id=?;";

    //language=SQL
    // private static final String SQL_SELECT_BY_LAST_NAME =
    //         "SELECT * FROM orders WHERE last_name LIKE ? ORDER BY id";

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final RowMapper<Order> customerRowMapper = (row, rowNumber) -> {
        int id = row.getInt("id");
        int product_id = row.getInt("product_id");
        int customer_id = row.getInt("customer_id");
        Timestamp order_date = row.getTimestamp("order_date");
        int quantity = row.getInt("quantity");
        int status_id = row.getInt("status_id");

        return new Order(id, product_id, customer_id, order_date, quantity, status_id, null);
    };

    @Override
    public List<Order> get(int id) {
        return jdbcTemplate.query(SQL_SELECT_BY_ID, customerRowMapper, id);
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, customerRowMapper);
    }

    @Override
    public void insert(Order order) {
        jdbcTemplate.update(
                SQL_INSERT,
                order.getProductId(),
                order.getCustomerId(),
                order.getQuantity(),
                order.getStatusId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(
                SQL_DELETE, id);
    }

    @Override
    public void nextPhase(int id) {
        jdbcTemplate.update(
                SQL_NEXT_PHASE, id);
    }
}
