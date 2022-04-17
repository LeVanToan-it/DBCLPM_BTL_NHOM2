use classicmodels;

delimiter //
create procedure GetOrderByCust(IN cust_no INT, 
								IN shipped INT,
								IN canceled INT,
                                OUT resolved INT,
                                OUT disputed INT)
begin
	-- shipped
	select count(*) into shipped from orders where customerNumber = cust_no AND status='Shipped';
    -- cancel
    select count(*) into canceled from orders where customerNumber = cust_no AND status='Canceled';
    -- resolve
    select count(*) into resolved from orders where customerNumber = cust_no AND status='Resolved';
    -- disput
    select count(*) into disputed from orders where customerNumber = cust_no AND status='Disputed';
end //
delimiter ;