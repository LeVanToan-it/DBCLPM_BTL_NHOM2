delimiter //

create procedure GetCustomerLevel(IN customerNo int, OUT customerLevel varchar(20))
begin
	declare credit decimal(10,2) default 0;
    
    -- get credit limit of customer
    select creditLimit into credit from customers where customerNumber = customerNo;
    
    -- call the function
    set customerLevel = CustomerLevel(credit);
end //

delimiter ;

call GetCustomerLevel(131,@customerLevel);
select @customerLevel;