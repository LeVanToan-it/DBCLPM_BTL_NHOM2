delimiter //

create procedure GetCustomerShipping(
IN pCustomerNumber INT,
OUT pShipping varchar(50)
)
begin
	declare customerCountry varchar(100);
    
    select country into customerCountry from customers where customerNumber=pCustomerNumber;
		case customerCountry
			when 'USA' then
				set pShipping='2-day Shipping';
			when 'Canada' then
				set pShipping='3-day Shipping';
			else
				set pShipping='5-day Shipping';
		end case;
end //
delimiter ;