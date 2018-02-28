DELIMITER ;;

CREATE FUNCTION get_join_table_name(ftable VARCHAR(50),
                                    stable VARCHAR(50)
                                    )
RETURNS VARCHAR(100)
BEGIN
  RETURN concat(ftable, '_', stable);
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lockable_trigger_event(evt INT)
RETURNS VARCHAR(10)
BEGIN
  IF evt = 0 THEN
    RETURN 'UPDATE';
  END IF;

  RETURN 'DELETE';
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lockable_trigger_name(entity VARCHAR(50),
                                          evt    INT
                                          )
RETURNS VARCHAR(100)
BEGIN
  IF evt = 0 THEN
    RETURN concat('tbu_lockable_', entity);
  END IF;

  RETURN concat('tbd_lockable_', entity);
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lockable_trigger_sql(entity VARCHAR(50),
                                         evt    INT
                                         )
RETURNS VARCHAR(1000)
BEGIN
  RETURN
  concat('CREATE TRIGGER ', get_lockable_trigger_name(entity, evt), ' BEFORE ', get_lockable_trigger_event(evt),
  ' ON ', entity, ' FOR EACH ROW ',
  'BEGIN',
  '  IF (SELECT 1 FROM ', get_join_table_name('LockGroup', entity), ' t where t.', entity, '_id=old.id limit 1) THEN',
  '    CALL throw_exception(''ERR_LOCK_001'');',
  'END IF;',
  'END;');
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lock_allowance_trigger_name(entity VARCHAR(50))
RETURNS VARCHAR(1000)
BEGIN
  RETURN concat('tbi_', get_join_table_name("LockGroup", entity));
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lock_allowance_trigger_sql(entity VARCHAR(50))
RETURNS VARCHAR(4000)
BEGIN
  DECLARE join_table VARCHAR(1000);

  SET join_table := get_join_table_name("LockGroup", entity);

  RETURN concat
  (
  ' CREATE TRIGGER ', get_lock_allowance_trigger_name(entity), '\n',
  ' BEFORE INSERT ', '\n',
  ' ON ', join_table, '\n',
  ' FOR EACH ROW ', '\n',
  ' BEGIN ', '\n',
  '   -- Check locking constraints: ', '\n',
  '   -- 1. There may be only one exclusive lock for the entity. ', '\n',
  '   -- 2. If there is any exclusive lock then no other locks can be established. ', '\n',
  '   -- 3. If there is any non exclusive locks then no exclusive lock can be established. ', '\n',
  '   DECLARE isExclusiveLock INTEGER; ', '\n',
  '   DECLARE hasOtherLocks INTEGER; ', '\n',
  '   DECLARE hasExclusiveLocks INTEGER; ', '\n',
  '   SET isExclusiveLock := (SELECT 1 FROM LockGroup WHERE id = NEW.LockGroup_id AND lockType = ''EXCLUSIVE_LOCK'' LIMIT 1); ', '\n',
  '   IF NOT isExclusiveLock IS NULL THEN ', '\n',
  '     SET hasOtherLocks := (SELECT 1 FROM ', join_table, ' WHERE ', entity, '_id = NEW.', entity, '_id LIMIT 1); ', '\n',
  '     IF NOT hasOtherLocks IS NULL THEN ', '\n',
  '       CALL throw_exception(''ERR_LOCK_002''); ', '\n',
  '     END IF; ', '\n',
  '   ELSE ', '\n',
  '     SET hasExclusiveLocks := ', '\n',
  '      ( ', '\n',
  '         SELECT 1 FROM ', join_table, ' ent, LockGroup lg ', '\n',
  '         WHERE ent.', entity, '_id = NEW.', entity, '_id ', '\n',
  '           AND ent.LockGroup_id = lg.id ', '\n',
  '           AND lg.lockType = ''EXCLUSIVE_LOCK'' ', '\n',
  '         LIMIT 1 ', '\n',
  '       ); ', '\n',
  '     IF NOT hasExclusiveLocks IS NULL THEN ', '\n',
  '       CALL throw_exception(''ERR_LOCK_003''); ', '\n',
  '     END IF; ', '\n',
  '   END IF; ', '\n',
  ' END; '
  );
END;;

DELIMITER ;

DELIMITER ;;

CREATE FUNCTION get_lock_owner_join_table_name(ftable VARCHAR(50),
                                               stable VARCHAR(50)
                                               )
RETURNS VARCHAR(100)
BEGIN
  RETURN concat(ftable, '_owner_', stable);
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE addLockOwner(IN entityName  VARCHAR(50),
                              IN entityId    BIGINT(20),
                              IN lockGroupId BIGINT(20)
                              )
BEGIN
  SET @lockGroup = 'LockGroup';
  CALL EXECUTE_IMMEDIATE(concat('INSERT INTO ', get_lock_owner_join_table_name(@lockGroup, entityName),
  ' (', @lockGroup, '_id,', entityName, '_id) VALUES (', lockGroupId, ',', entityId, ')'));
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE create_join_table(IN ftable VARCHAR(50),
                                   IN stable VARCHAR(50)
                                   )
BEGIN
  CALL EXECUTE_IMMEDIATE(
  concat('CREATE TABLE IF NOT EXISTS ', get_join_table_name(ftable, stable), ' (',
  ftable, '_id BIGINT(20) NOT NULL, ',
  stable, '_id BIGINT(20) NOT NULL, ',
  'PRIMARY KEY(', ftable, '_id, ', stable, '_id), ',
  'INDEX USING BTREE (', ftable, '_id), ',
  'INDEX USING BTREE (', stable, '_id), ',
  'CONSTRAINT FOREIGN KEY (', ftable, '_id) ',
  'REFERENCES ', ftable, ' (id) ON DELETE CASCADE, ',
  'CONSTRAINT FOREIGN KEY (', stable, '_id) ',
  'REFERENCES ', stable, ' (id)',
  ')')
  );
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE create_lock_owner_table(IN ftable    VARCHAR(50),
                                         IN lockowner VARCHAR(50)
                                         )
BEGIN
  CALL EXECUTE_IMMEDIATE(
  concat('CREATE TABLE IF NOT EXISTS ', get_lock_owner_join_table_name(ftable, lockowner), ' (',
  ftable, '_id BIGINT(20) NOT NULL, ',
  lockowner, '_id BIGINT(20) NOT NULL, ',
  'lockOwnerId BIGINT(20) NOT NULL, ',
  'PRIMARY KEY(', ftable, '_id, ', lockowner, '_id), ',
  'INDEX USING BTREE (', ftable, '_id), ',
  'INDEX USING BTREE (', lockowner, '_id), ',
  'UNIQUE INDEX USING BTREE (LockOwnerID, ', lockowner, '_id), ',
  'CONSTRAINT FOREIGN KEY (', ftable, '_id) ',
  'REFERENCES ', ftable, ' (id) ON DELETE CASCADE, ',
  'CONSTRAINT FOREIGN KEY (', lockowner, '_id) ',
  'REFERENCES ', lockowner, ' (id)',
  ')')
  );
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE execute_immediate(IN dsql TEXT)
BEGIN
  SET @s = dsql;
  PREPARE stat FROM @s;
  EXECUTE stat;
  DEALLOCATE PREPARE stat;
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE is_lock_entity(IN  entityName VARCHAR(50),
                                IN  entityId   BIGINT(20),
                                OUT result     INT(1)
                                )
BEGIN
  SET @lockGroup = 'LockGroup';
  SET @res = 0;
  SET @s = concat('SELECT 1 INTO @res FROM ', get_join_table_name(@lockGroup, entityName),
  ' WHERE ', entityName, '_id=', entityId, ' LIMIT 1');

  CALL execute_immediate(@s);
  SET result = @res;
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE lock_entity(IN entityName  VARCHAR(50),
                             IN entityId    BIGINT(20),
                             IN lockGroupId BIGINT(20)
                             )
BEGIN
  SET @lockGroup = 'LockGroup';
  CALL EXECUTE_IMMEDIATE(concat('INSERT INTO ', get_join_table_name(@lockGroup, entityName),
  ' (', @lockGroup, '_id,', entityName, '_id) VALUES (', lockGroupId, ',', entityId, ')'));
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE registerLockOwner(IN entity VARCHAR(50))
BEGIN
  DECLARE eid BIGINT(20);

  SELECT t.id
  INTO
    eid
  FROM
    LockGroupOwner t
  WHERE
    upper(t.entity) = upper(entity);
  IF eid IS NULL THEN
    INSERT INTO LockGroupOwner (entity) VALUES (entity);
  ELSE
    UPDATE LockGroupOwner
    SET
      dropTable = 'n'
    WHERE
      id = eid;
  END IF;
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE register_lockable(IN entity VARCHAR(50))
BEGIN
  DECLARE eid BIGINT(20);

  SELECT t.id
  INTO
    eid
  FROM
    LockGroupItem t
  WHERE
    upper(t.entity) = upper(entity);
  IF eid IS NULL THEN
    INSERT INTO LockGroupItem (entity) VALUES (entity);
  ELSE
    UPDATE LockGroupItem
    SET
      dropTable = 'n'
    WHERE
      id = eid;
  END IF;
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE throw_exception(IN exception_text VARCHAR(255))
BEGIN
  INSERT INTO ExceptionGenerator (dummy) VALUES (exception_text);
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE unlockLockGroupsOfOwner(IN entityName  VARCHAR(50),
                                         IN lockOwnerId BIGINT(20)
                                         )
BEGIN
  SET @s =
  concat(
  'DELETE FROM LockGroup ',
  'WHERE EXISTS (',
  '  SELECT 1 FROM ', get_lock_owner_join_table_name('LockGroup', entityName), ' a ',
  '  WHERE a.LockGroup_id = LockGroup.id and a.', entityName, '_id=', lockOwnerId,
  '   LIMIT 1);'
  );
  CALL EXECUTE_IMMEDIATE(@s);
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE unlock_entity(IN entityName  VARCHAR(50),
                               IN entityId    BIGINT(20),
                               IN lockGroupId BIGINT(20)
                               )
BEGIN
  SET @lockGroup = 'LockGroup';
  SET @s = concat('DELETE FROM ', get_join_table_name(@lockGroup, entityName),
  ' WHERE ', entityName, '_id=', entityId);
  IF (lockGroupId <> 0) THEN
    SET @s = concat(@s, ' AND ', @lockGroup, '_id=', lockGroupId);
  END IF;
  CALL EXECUTE_IMMEDIATE(@s);
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE updateLockOwner()
BEGIN
  DECLARE done   INT DEFAULT 0;
  DECLARE jtable VARCHAR(100);
  DECLARE sle CURSOR FOR SELECT t.entity
                         FROM
                           LockGroupOwner t
                         WHERE
                           upper(t.dropTable) = 'N';
  DECLARE not_sle CURSOR FOR SELECT t.entity
                             FROM
                               LockGroupOwner t
                             WHERE
                               upper(t.dropTable) <> 'N';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  SET @lockGroup = 'LockGroup';

  OPEN sle;
  FETCH sle INTO jtable;
  WHILE NOT done
  DO
    CALL create_lock_owner_table(@lockGroup, jtable);
    FETCH sle INTO jtable;
  END WHILE;
  CLOSE sle;

  SET done = 0;
  OPEN not_sle;
  FETCH not_sle INTO jtable;
  WHILE NOT done
  DO
    CALL EXECUTE_IMMEDIATE(concat('DROP TABLE IF EXISTS ', get_lock_owner_join_table_name(@lockGroup, jtable)));
    FETCH not_sle INTO jtable;
  END WHILE;
  CLOSE not_sle;

  DELETE
  FROM
    LockGroupOwner
  WHERE
    upper(dropTable) <> 'N';

  UPDATE LockGroupOwner
  SET
    dropTable = 'Y';
END;;

DELIMITER ;

DELIMITER ;;

CREATE PROCEDURE update_lockable()
BEGIN
  DECLARE done   INT DEFAULT 0;
  DECLARE jtable VARCHAR(100);
  DECLARE sle CURSOR FOR SELECT t.entity
                         FROM
                           LockGroupItem t
                         WHERE
                           upper(t.dropTable) = 'N';
  DECLARE not_sle CURSOR FOR SELECT t.entity
                             FROM
                               LockGroupItem t
                             WHERE
                               upper(t.dropTable) <> 'N';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  SET @lockGroup = 'LockGroup';

  OPEN sle;
  FETCH sle INTO jtable;
  WHILE NOT done
  DO
    CALL create_join_table(@lockGroup, jtable);


    FETCH sle INTO jtable;
  END WHILE;
  CLOSE sle;

  SET done = 0;
  OPEN not_sle;
  FETCH not_sle INTO jtable;
  WHILE NOT done
  DO
    CALL EXECUTE_IMMEDIATE(concat('DROP TABLE IF EXISTS ', get_join_table_name(@lockGroup, jtable)));


    FETCH not_sle INTO jtable;
  END WHILE;
  CLOSE not_sle;

  DELETE
  FROM
    LockGroupItem
  WHERE
    upper(dropTable) <> 'N';

  UPDATE LockGroupItem
  SET
    dropTable = 'Y';
END;;

DELIMITER ;

