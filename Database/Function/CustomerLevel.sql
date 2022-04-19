delimiter //

create function CustomerLevel(credit decimal(10,2)) returns varchar(20)
deterministic
begin
	declare customerLevel varchar(20);
    if credit > 50000 then
		set customerLevel = 'PLATINUM';
	elseif (credit >= 10000 and credit <= 50000) then 
		set customerLevel = 'GOLD';
	elseif credit < 10000 then
		set customerLevel = 'SILVER';
	end if ;
    return customerLevel;
end //

delimiter ;

show function status where db = 'classicmodels';

select CustomerLevel(creditLimit) from customers;