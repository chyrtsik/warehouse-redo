package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.directory.Car;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.cars.CarTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 2013-01-23
 */
public class CarTransformer {

    private CarTransformer() {
    }

    public static CarTO transform(Car car) {
        if (car == null) {
            return null;
        }
        CarTO carTO = new CarTO();
        update(carTO, car);
        return carTO;
    }

    public static List<CarTO> transformList(List<Car> cars) {
        List<CarTO> list = new ArrayList<CarTO>();
        for (Car car : cars) {
            list.add(transform(car));
        }
        return list;
    }

    public static Car transform(CarTO carTO) {
        if (carTO == null) {
            return null;
        }
        Car car = SpringServiceContext.getInstance().getCarService().getCarById(carTO.getId());
        if (car == null) {
            car = new Car();
        }
        return car;
    }

    public static void update(Car car, CarTO carTO) {
        car.setId(carTO.getId());
        car.setBrand(carTO.getBrand());
        car.setStateNumber(carTO.getStateNumber());
        car.setFullName(carTO.getFullName());
        car.setOwner(carTO.getOwner());
        car.setTrailer(carTO.getTrailer());
        car.setDescription(carTO.getDescription());
    }

    public static void update(CarTO carTO, Car car) {
        carTO.setId(car.getId());
        carTO.setBrand(car.getBrand());
        carTO.setStateNumber(car.getStateNumber());
        carTO.setFullName(car.getFullName());
        carTO.setOwner(car.getOwner());
        carTO.setTrailer(car.getTrailer());
        carTO.setDescription(car.getDescription());
    }
}
