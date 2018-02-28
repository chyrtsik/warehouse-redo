-- Create table for detail batch history.
CREATE TABLE DetailBatchOperation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  operationDateTime DATETIME NOT NULL,
  performedUser_id BIGINT(20) NOT NULL,
  operationType VARCHAR(255) NOT NULL,
  detailBatch_id BIGINT(20) NOT NULL,
  storagePlace_id BIGINT(20) NOT NULL,
  postingItem_id BIGINT(20) NULL,
  chargeOffItem_id BIGINT(20) NULL,
  initialCount BIGINT(20) NOT NULL,
  newCount BIGINT(20) NOT NULL,
  changeOfCount BIGINT(20) NOT NULL,
  PRIMARY KEY (id),
  INDEX detailBatch_id (detailBatch_id),
  INDEX performedUser_id (performedUser_id),
  INDEX storagePlace_id (storagePlace_id),
  INDEX postingItem_id (postingItem_id),
  INDEX chargeOffItem_id (chargeOffItem_id),
  CONSTRAINT DetailBatchHistory_ibfk_1 FOREIGN KEY (detailBatch_id)
  REFERENCES DetailBatch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT DetailBatchHistory_ibfk_2 FOREIGN KEY (performedUser_id)
  REFERENCES User (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT DetailBatchHistory_ibfk_3 FOREIGN KEY (storagePlace_id)
  REFERENCES StoragePlace (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT DetailBatchHistory_ibfk_4 FOREIGN KEY (postingItem_id)
  REFERENCES PostingItem (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT DetailBatchHistory_ibfk_5 FOREIGN KEY (chargeOffItem_id)
  REFERENCES ChargeOffItem (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);