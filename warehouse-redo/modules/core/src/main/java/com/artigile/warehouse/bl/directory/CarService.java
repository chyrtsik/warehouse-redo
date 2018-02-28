package com.artigile.warehouse.bl.directory;

import com.artigile.warehouse.dao.CarDAO;
import com.artigile.warehouse.domain.directory.Car;
import com.artigile.warehouse.utils.dto.cars.CarTO;
import com.artigile.warehouse.utils.transofmers.CarTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Valery Barysok, 2013-01-23
 */
@Transactional
public class CarService {

    private CarDAO carDAO;

    public Car getCarById(long carId) {
        return carDAO.get(carId);
    }

    public List<CarTO> getAll() {
        return CarTransformer.transformList(carDAO.getAll());
    }

    public void save(CarTO carTO) {
        Car persistentCar = CarTransformer.transform(carTO);
        CarTransformer.update(persistentCar, carTO);
        carDAO.save(persistentCar);
        CarTransformer.update(carTO, persistentCar);
    }

    public void remove(CarTO carTO) {
        Car car = carDAO.get(carTO.getId());
        if (car != null) {
            carDAO.remove(car);
        }
    }

    public void setCarDAO(CarDAO carDAO) {
        this.carDAO = carDAO;
    }
}
