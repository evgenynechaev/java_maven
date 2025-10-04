package ru.rtk.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rtk.model.Customer;

import javax.sql.DataSource;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    //language=SQL
    private static final String SQL_INSERT =
            "INSERT INTO customers(first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";

    //language=SQL
    private static final String SQL_DELETE =
            "DELETE FROM customers WHERE id=?";

    //language=SQL
    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM customers WHERE id=?";

    //language=SQL
    private static final String SQL_SELECT_ALL =
            "SELECT * FROM customers ORDER BY id";

    //language=SQL
    private static final String SQL_SELECT_BY_NAME =
            "SELECT * FROM customers WHERE last_name ILIKE ? OR first_name ILIKE ? ORDER BY id";

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final RowMapper<Customer> customerRowMapper = (row, rowNumber) -> {
        int id = row.getInt("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String phone = row.getString("phone");
        String email = row.getString("email");

        return new Customer(id, firstName, lastName, phone, email);
    };

    @Override
    public List<Customer> get(int id) {
        return jdbcTemplate.query(SQL_SELECT_BY_ID, customerRowMapper, id);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, customerRowMapper);
    }

    @Override
    public List<Customer> findByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_BY_NAME, customerRowMapper, name, name);
    }

    @Override
    public void insert(Customer customer) {
        jdbcTemplate.update(
                SQL_INSERT,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getEmail());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(
                SQL_DELETE, id);
    }
}
