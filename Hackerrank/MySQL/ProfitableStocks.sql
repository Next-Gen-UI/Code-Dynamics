-- https://www.youtube.com/watch?v=KihxBmbb3Jg
SELECT s.stock_code
FROM price_today as s, price_tomorrow as p
WHERE p.stock_code = s.stock_code and p.price > s.price;