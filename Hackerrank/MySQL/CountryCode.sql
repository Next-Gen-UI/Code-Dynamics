select a.customer_id,a.name,concat("+",b.country_code,a.phone_number)
from customers as a
left join country_codes as b on a.country=b.country
order by a.customer_id