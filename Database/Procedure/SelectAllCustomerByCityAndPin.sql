use classicmodels;

delimiter //

create procedure SelectAllCustomerByCityAndPin(IN mycity varchar(50), IN pcode varchar(15))
begin
	select * from customers where city=mycity and postalCode=pcode;
end //

delimiter ;