use classicmodels;

delimiter //

create procedure SelectAllCustomer()
begin
	select * from customers;
end //

delimiter ;