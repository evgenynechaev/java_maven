package ru.rtk.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rtk.model.Customer;
import ru.rtk.model.Status;

import javax.sql.DataSource;
import java.util.List;

public class OrderStatusRepositoryImpl implements OrderStatusRepository {
    //language=SQL
    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM order_status WHERE id=?";

    //language=SQL
    private static final String SQL_SELECT_ALL =
            "SELECT * FROM order_status ORDER BY id";

    //language=SQL
    private static final String SQL_SELECT_BY_NAME =
            "SELECT * FROM order_status WHERE name LIKE ? ORDER BY id";

    private final JdbcTemplate jdbcTemplate;

    public OrderStatusRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final RowMapper<Status> statusRowMapper = (row, rowNumber) -> {
        int id = row.getInt("id");
        String name = row.getString("name");

        return new Status(id, name);
    };

    @Override
    public List<Status> get(int id) {
        return jdbcTemplate.query(SQL_SELECT_BY_ID, statusRowMapper, id);
    }

    @Override
    public List<Status> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, statusRowMapper);
    }

    @Override
    public List<Status> findByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_BY_NAME, statusRowMapper, name);
    }
}
