-- создание базы данных

DROP TABLE orders;
DROP TABLE customers;
DROP TABLE products;
DROP TABLE order_status;

CREATE TABLE IF NOT EXISTS products
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    category VARCHAR(30) NOT NULL
);
COMMENT ON COLUMN products.name IS 'Наименование продукта';
COMMENT ON COLUMN products.price IS 'Цена продукта';
COMMENT ON COLUMN products.quantity IS 'Количество на складе';
COMMENT ON COLUMN products.category IS 'Категория продукта';

CREATE TABLE IF NOT EXISTS customers
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(50) UNIQUE
);
COMMENT ON COLUMN customers.first_name IS 'Имя клиента';
COMMENT ON COLUMN customers.last_name IS 'Фамилия клиента';
COMMENT ON COLUMN customers.phone IS 'Телефон';
COMMENT ON COLUMN customers.email IS 'Эл.почта';

CREATE TABLE IF NOT EXISTS order_status
(
    id INTEGER PRIMARY KEY,
    name VARCHAR(10) UNIQUE NOT NULL
);
COMMENT ON COLUMN order_status.name IS 'Название статуса заказа';

CREATE TABLE IF NOT EXISTS orders
(
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products (id),
    customer_id INTEGER REFERENCES customers (id),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status_id INTEGER REFERENCES order_status (id)
);
COMMENT ON COLUMN orders.order_date IS 'Дата размещения заказа';
COMMENT ON COLUMN orders.status_id IS 'Идентификатор текущего статуса заказа';

-- CREATE INDEX idx_products_fk ON orders(product_id);
-- CREATE INDEX idx_customers_fk ON orders(customer_id);
-- CREATE INDEX idx_order_date ON orders(date_order);

TRUNCATE orders CASCADE;
TRUNCATE customers CASCADE;
TRUNCATE products CASCADE;
TRUNCATE order_status CASCADE;

-- добавление информации
INSERT INTO order_status(id, name)
VALUES
    (0,'Новый'),
    (1,'Оплачен'),
    (2,'Отгружен'),
    (3,'Завершен');

INSERT INTO products(name, price, quantity, category)
VALUES ('Скороварка Bosch A-100', 5000, 10, 'Кухонная техника');

INSERT INTO products(name, price, quantity, category)
VALUES ('Телевизор Samsung AAA', 30000, 50, 'Аудио-видео техника');

INSERT INTO products(name, price, quantity, category)
VALUES ('Пылесос Philips BBB', 6000, 20, 'Бытовая техника');

INSERT INTO products(name, price, quantity, category)
VALUES ('Фен Philips CCC', 2000, 30, 'Бытовая техника');

INSERT INTO customers(first_name, last_name, phone, email)
VALUES ('Иван', 'Иванов', '+7(900)1234567', 'aaa@mail.ru');

INSERT INTO customers(first_name, last_name, phone, email)
VALUES ('Петр', 'Петров', '+7(900)1234568', 'bbb@mail.ru');

INSERT INTO customers(first_name, last_name, phone, email)
VALUES ('Вася', 'Васечкин', '+7(900)1234569', 'ccc@mail.ru');

INSERT INTO orders(product_id, customer_id, quantity, status_id) VALUES (1, 1, 2, 1);
INSERT INTO orders(product_id, customer_id, quantity, status_id) VALUES (2, 2, 3, 2);
