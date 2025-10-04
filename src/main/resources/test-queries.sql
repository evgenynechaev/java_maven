-- Тестовые запросы

--
-- Заказы за последние 7 дней
SELECT
    o.id AS "order id",
    c.first_name || ' ' || c.last_name AS "customer",
    p.name AS "product",
    o.order_date AS "order date",
    os.name AS "order status"
FROM orders o
    LEFT OUTER JOIN customers c ON c.id=o.customer_id
    LEFT OUTER JOIN products p ON p.id = o.product_id
    LEFT OUTER JOIN order_status os ON os.id = o.status_id
WHERE o.order_date > current_date - INTERVAL '7 days';

--
-- Заказы в статусе оплачен и отгружен
SELECT
    o.id AS "order id",
    c.first_name || ' ' || c.last_name AS "customer",
    p.name AS "product",
    o.order_date AS "order date",
    os.name AS "order status"
FROM orders o
    LEFT OUTER JOIN customers c ON c.id=o.customer_id
    LEFT OUTER JOIN products p ON p.id = o.product_id
    LEFT OUTER JOIN order_status os ON os.id = o.status_id
WHERE os.name ILIKE 'оплачен' OR os.name ILIKE 'отгружен';

--
-- 3 самых популярных товара за 6 месяцев
SELECT
    o.product_id, p.name, sum(o.quantity) AS "total"
FROM orders o
    LEFT OUTER JOIN customers c ON c.id=o.customer_id
    LEFT OUTER JOIN products p ON p.id = o.product_id
    LEFT OUTER JOIN order_status os ON os.id = o.status_id
WHERE o.order_date > current_date - INTERVAL '6 month'
GROUP BY o.product_id, p.name
ORDER BY total DESC
LIMIT 3;

--
-- Товары на складе в категории 'Бытовая техника'
SELECT * FROM products WHERE category ILIKE 'бытовая техника' ORDER BY name;

--
-- Покупатели с именем Иван, которые сделали хотя бы 1 оплаченный заказ
SELECT
    o.customer_id, c.last_name, c.first_name, count(o.id) AS "count"
FROM orders o
         LEFT OUTER JOIN customers c ON c.id=o.customer_id
         LEFT OUTER JOIN products p ON p.id = o.product_id
         LEFT OUTER JOIN order_status os ON os.id = o.status_id
WHERE c.first_name ilike 'иван'
GROUP BY o.customer_id, c.last_name, c.first_name
ORDER BY count DESC;

--
-- Удалить клиентов, которые не сделали ни одного заказа
WITH no_orders AS (
    SELECT c.id
    FROM customers c
        LEFT OUTER JOIN orders o ON c.id=o.customer_id
    WHERE o.customer_id IS NULL
)
DELETE FROM customers c USING no_orders AS n WHERE c.id=n.id;

--
-- Удалить все заказы в статусе 'Завершен' старше одного года
DELETE FROM orders o WHERE o.id IN (
    SELECT o.id
    FROM orders o
        LEFT OUTER JOIN order_status os ON os.id = o.status_id
    WHERE o.order_date > current_date - INTERVAL '1 year' AND os.name ILIKE 'завершен'
);
