delimiter //

create trigger AfterMembersInsert after insert on members for each row
begin
	if NEW.birthDate is null then insert into reminders(memberId,message)
		values(new.id,concat('Hi ',new.name,', please update your date of birth.'));
	end if;
end //

delimiter ;