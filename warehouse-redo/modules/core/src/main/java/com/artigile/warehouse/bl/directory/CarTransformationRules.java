package com.artigile.warehouse.bl.directory;

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.directory.Car;
import com.artigile.warehouse.utils.dto.cars.CarTO;
import com.artigile.warehouse.utils.transofmers.CarTransformer;

/**
 * @author Valery Barysok, 2013-01-23
 */
public class CarTransformationRules {

    public CarTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getCarToCarTORule());
    }

    private EntityTransformRule getCarToCarTORule() {
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Car.class);
        rule.setTargetClass(CarTO.class);
        rule.setEntityTransformer(new EntityTransformer() {
            @Override
            public Object transform(Object entity) {
                return CarTransformer.transform((Car) entity);
            }
        });
        return rule;
    }
}
