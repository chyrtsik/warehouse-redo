package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.dao.DetailFieldDAO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProviderFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provider of dynamic detail batch fields used in printing.
 *
 * @author Aliaksandr.Chyrtsik, 20.12.12
 */
@Transactional(readOnly = true)
public class DetailBatchDynamicFieldsProvider implements DynamicObjectFieldsProvider{
    private static final String PROVIDER_NAME = "detailBatchDynamicFieldsProvider";

    private DetailTypeService detailTypeService;

    public void initialize(){
        DynamicObjectFieldsProviderFactory.getInstance().registerProvider(PROVIDER_NAME, this);
    }

    @Override
    public List<String> getAvailableFields() {
        return detailTypeService.getAllUniqueDetailFieldNames();
    }

    @Override
    public Class getFieldValueClass(String dynamicField) {
        //We no not provide strong typing for now -- the same named field for different
        //detail types can have different types.
        return Object.class;
    }

    @Override
    public Object getFieldValue(Object object, String dynamicField) {
        if (object == null){
            return null;
        }
        else if (object instanceof DetailBatchTO){
            DetailBatchTO detailBatch = (DetailBatchTO)object;
            int fieldIndex = detailBatch.getFieldIndexByName(dynamicField);
            return fieldIndex == -1 ? null : detailBatch.getFieldValue(fieldIndex);
        }
        else{
            throw new IllegalArgumentException("Invalid object type passed to " + PROVIDER_NAME + ": " + object.toString());
        }
    }

    @Override
    public void setFieldValue(Object object, String dynamicField, Object value) {
        //Setting of fields values is not provided yet.
        LoggingFacade.logWarning(this, "Calling of not implemented method: setFieldValue");
    }

    public void setDetailTypeService(DetailTypeService detailTypeService) {
        this.detailTypeService = detailTypeService;
    }
}
