select ei.employee_ID, ei.name
from employee_information ei, last_quarter_bonus lb
where ei.employee_ID = lb.employee_ID and ei.division  = 'HR' and lb.bonus >= 5000