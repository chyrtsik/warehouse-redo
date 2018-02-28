alter table contractorproduct
  add column sourceData TEXT DEFAULT NULL;

update contractorproduct
   set sourceData=(select sourceData
                     from contractorproduct_sourcedata a
                    where a.contractorProduct_id=contractorproduct.id limit 1);
