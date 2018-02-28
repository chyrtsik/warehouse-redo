-- Change movement date field to date time.

alter table movement
  modify createDate DATETIME NOT NULL,
  modify beginDate DATETIME DEFAULT NULL,
  modify endDate DATETIME DEFAULT NULL;
