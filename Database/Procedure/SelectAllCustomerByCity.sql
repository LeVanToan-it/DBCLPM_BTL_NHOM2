use classicmodels;

delimiter //

create procedure SelectAllCustomerByCityAndPin(IN mycity varchar(50))
begin
	select * from customers where city=mycity;
end //

delimiter ;