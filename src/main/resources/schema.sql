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

/*
-- создание таблицы
CREATE TABLE IF NOT EXISTS products
(
    id         serial primary key, -- идентификатор строки, всегда уникальный, генерируется базой данных
    -- определяем столбцы
    first_name varchar(20),        -- строка максимальной длины 20
    last_name  varchar(20),
    age        integer check (age > 0 and age < 120)
);

CREATE TABLE IF NOT EXISTS customers
(
    id         serial primary key, -- идентификатор строки, всегда уникальный, генерируется базой данных
    -- определяем столбцы
    first_name varchar(20),        -- строка максимальной длины 20
    last_name  varchar(20),
    age        integer check (age > 0 and age < 120)
);

CREATE TABLE IF NOT EXISTS orders
(
    id         serial primary key, -- идентификатор строки, всегда уникальный, генерируется базой данных
    -- определяем столбцы
    first_name varchar(20),        -- строка максимальной длины 20
    last_name  varchar(20),
    age        integer check (age > 0 and age < 120)
);



CREATE TABLE product
(
    id         serial primary key, -- идентификатор строки, всегда уникальный, генерируется базой данных
    -- определяем столбцы
    first_name varchar(20),        -- строка максимальной длины 20
    last_name  varchar(20),
    age        integer check (age > 0 and age < 120)
);


-- добавление информации
insert into account(first_name, last_name, age)
values ('Роман', 'Полански', 45);
insert into account(first_name, last_name, age)
values ('Виктор', 'Китчет', 23);
insert into account(first_name, last_name, age)
values ('Наташа', 'Романова', 12);
insert into account(first_name, last_name, age)
values ('Ваня', 'Миниахметов', 43);
insert into account(first_name, last_name, age)
values ('Тимур', 'Неверов', 78);

-- обновление информации
update account
set age = 28
where id = 1;

-- получение всех данных из таблицы
select *
from account;

-- получить только имена
select first_name
from account;

-- получить имя и возраст с сортировкой по возрастанию возраста
select first_name, age
from account
order by age;

-- получить имя и возраст с сортировкой по убиванию возраста
select first_name, age
from account
order by age desc;

-- получить всех, кто старше 24 лет
select *
from account
where age > 24;

-- сколько людей старше 24-х лет
select count(*)
from account
where age > 24;

-- какие возраста встречаются?
select distinct(age)
from account;

-- какие возраста и сколько раз встречаются
select age, count(*)
from account
group by age;

create table car
(
    id       serial primary key,
    model    varchar(20),
    color    varchar(20),
    owner_id integer,
    -- внешний ключ, ссылка на строку из другой таблицы
    foreign key (owner_id) references account (id)
);

insert into car(model, color, owner_id)
values ('BMW', 'Black', 5);
insert into car(model, color, owner_id)
values ('Lada', 'Green', 5);
insert into car(model, color, owner_id)
values ('Kia', 'White', 1);
insert into car(model, color, owner_id)
values ('Toyota', 'Black', 2);
insert into car(model, color, owner_id)
values ('Toyota', 'Aqa', 3);
insert into car(model, color)
values ('Bugatti', 'Blue');

-- получить имя пользователя с id 5 и количество его машин
select first_name, (select count(*) from car where owner_id = 5) as cars_count
from account
where id = 5;

-- получить имена пользователей  и количество его машин
select first_name, (select count(*) from car where owner_id = account.id) as cars_count
from account;

-- получить имена пользователей, у которых машины черного цвета
select first_name
from account
where account.id in
      (select owner_id from car where color = 'Black');

-- все владельцы, и машины у которых есть владелец
select *
from account a
         left join car c on a.id = c.owner_id;

-- все машины, и владельцы у которых есть машины
select *
from account a
         right join car c on a.id = c.owner_id;

-- только машины и владельцы, которые есть друг у друга
select *
from account a
         inner join car c on a.id = c.owner_id;

-- все владельцы, все машины
select *
from account a
         full join car c on a.id = c.owner_id;
*/
