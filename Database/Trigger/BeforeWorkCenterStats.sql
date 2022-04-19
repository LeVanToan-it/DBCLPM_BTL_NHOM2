delimiter //

create trigger BeforeWorkCentersInsert before insert on workcenters for each row
begin
	declare rowcount int;
    
    select count(*) into rowcount from workcenterstats;
    
    if rowcount > 0 then
		update workcenterstats set totalCapacity = totalCapacity + new.capacity;
	else
		insert into workcenterstats(totalCapacity) values(new.capacity);
	end if ;
end //

delimiter ;

show triggers;