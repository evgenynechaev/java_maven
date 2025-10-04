package ru.rtk.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rtk.model.Product;

import javax.sql.DataSource;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    //language=SQL
    private static final String SQL_INSERT =
            "INSERT INTO products(name, price, quantity, category) VALUES (?, ?, ?, ?)";

    //language=SQL
    private static final String SQL_DELETE =
            "DELETE FROM products WHERE id=?";

    private static final String SQL_ADD_QUANTITY =
            "UPDATE products SET quantity=quantity+? WHERE id=?;";

    private static final String SQL_SUBTRACT_QUANTITY =
            "UPDATE products SET quantity=quantity-? WHERE id=?;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM products WHERE id=?";

    //language=SQL
    private static final String SQL_SELECT_ALL =
            "SELECT * FROM products ORDER BY name";

    //language=SQL
    private static final String SQL_SELECT_BY_NAME =
            "SELECT * FROM products WHERE name ILIKE ? ORDER BY id";

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final RowMapper<Product> productRowMapper = (row, rowNumber) -> {
        int id = row.getInt("id");
        String name = row.getString("name");
        double price = row.getDouble("price");
        int quantity = row.getInt("quantity");
        String category = row.getString("category");

        return new Product(id, name, price, quantity, category);
    };

    @Override
    public List<Product> get(int id) {
        return jdbcTemplate.query(SQL_SELECT_BY_ID, productRowMapper, id);
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, productRowMapper);
    }

    @Override
    public List<Product> findByName(String name) {
        return jdbcTemplate.query(SQL_SELECT_BY_NAME, productRowMapper, name);
    }

    @Override
    public void insert(Product product) {
        jdbcTemplate.update(
                SQL_INSERT,
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(
                SQL_DELETE, id);
    }

    @Override
    public void add(int id, int quantity) {
        jdbcTemplate.update(
                SQL_ADD_QUANTITY, quantity, id);
    }

    @Override
    public void subtract(int id, int quantity) {
        jdbcTemplate.update(
                SQL_SUBTRACT_QUANTITY, quantity, id);
    }
}
