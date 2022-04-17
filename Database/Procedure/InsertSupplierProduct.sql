delimiter //

create procedure InsertSupplierProduct(IN inSupplierId INT, IN inProductId INT)
begin
	-- exit if the duplicate key occur
	declare exit handler for 1062 select 'Duplicate keys error encountered' Message;
    declare exit handler for sqlexception select 'SQLException encountered' Message;
    declare exit handler for sqlstate '23000' select 'SQLSTATE 23000' Message;
    
    -- insert a new row
    insert into supplierproducts(supplierId,productId) values(inSupplierId,inProductId);
    
	-- return the products supplied by the supplier Id
	select count(*) from supplierproducts where supplierId=inSupplierId;
end //

delimiter ;