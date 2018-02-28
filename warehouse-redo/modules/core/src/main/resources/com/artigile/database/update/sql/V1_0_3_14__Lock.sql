drop procedure if exists create_lock_owner_table;

delimiter ;;

create procedure create_lock_owner_table(in ftable    varchar(50),
                                         in lockowner varchar(50)
                                         )
begin
  call EXECUTE_IMMEDIATE(
  concat('CREATE TABLE IF NOT EXISTS ', get_lock_owner_join_table_name(ftable, lockowner), ' (',
  ftable, '_id BIGINT(20) NOT NULL, ',
  lockowner, '_id BIGINT(20) NOT NULL, ',
  'PRIMARY KEY(', ftable, '_id, ', lockowner, '_id), ',
  'INDEX USING BTREE (', ftable, '_id), ',
  'INDEX USING BTREE (', lockowner, '_id), ',
  'CONSTRAINT FOREIGN KEY (', ftable, '_id) ',
  'REFERENCES ', ftable, ' (id) ON DELETE CASCADE, ',
  'CONSTRAINT FOREIGN KEY (', lockowner, '_id) ',
  'REFERENCES ', lockowner, ' (id)',
  ')')
  );
end;;

delimiter ;
