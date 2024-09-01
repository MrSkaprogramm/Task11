package com.andersen.tr.task11;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.service.impl.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCarTest {
    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarService(carRepository, "Test conditional bean");
    }

    static List<Arguments> providerPositiveTest() {
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of(1, "Toyota", CarType.SEDAN));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(int carId, String newBrand, CarType newType) {
        Car car = new Car();
        car.setId(carId);
        car.setBrand("Honda");
        car.setType(CarType.SEDAN);

        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Mockito.when(carRepository.save(Mockito.any(Car.class))).thenAnswer(invocation -> {
            Car savedCar = invocation.getArgument(0);
            savedCar.setId(carId);
            return savedCar;
        });

        Car updatedCar = carService.updateCar(carId, newBrand, newType);

        assertNotNull(updatedCar, "Updated car should not be null");
        assertEquals(newBrand, updatedCar.getBrand());
        assertEquals(newType, updatedCar.getType());
        assertEquals(carId, updatedCar.getId());

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        Mockito.verify(carRepository).save(carCaptor.capture());
        assertEquals(newBrand, carCaptor.getValue().getBrand());
        assertEquals(newType, carCaptor.getValue().getType());
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of(999, "InvalidBrand", CarType.SEDAN));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void negativeTest(int carId, String newBrand, CarType newType) {
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            carService.updateCar(carId, newBrand, newType);
        });

        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(carRepository).findById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue());
    }

    static List<Arguments> providerCornerCaseTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(1, " Honda ", CarType.SEDAN));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerCornerCaseTest")
    public void cornerCaseTest(int carId, String brandWithSpaces, CarType carType) {
        Car car = new Car();
        car.setId(carId);
        car.setBrand("Toyota");
        car.setType(CarType.PICKUP);

        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car updatedCar = carService.updateCar(carId, brandWithSpaces, carType);

        assertNotNull(updatedCar, "Updated car should not be null");
        assertEquals(brandWithSpaces.trim(), updatedCar.getBrand());
        assertEquals(carType, updatedCar.getType());
        assertEquals(carId, updatedCar.getId());
    }
}
