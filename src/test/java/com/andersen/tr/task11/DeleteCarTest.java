package com.andersen.tr.task11;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.service.impl.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteCarTest {
    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarService(carRepository, null);
    }

    static List<Arguments> providerPositiveTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(1, "Toyota", CarType.SEDAN));
        args.add(Arguments.of(2, "Honda", CarType.COUPE));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(int carId, String brand, CarType type) {
        Car car = new Car(carId, brand, type);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);

        String result = carService.deleteCar(carId);
        assertEquals("Car deleted", result, "Should return 'Car deleted'");

        verify(carRepository).deleteById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue(), "Car ID should match");
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(1));
        args.add(Arguments.of(2));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void testNegativeTest(int carId) {
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        String result = carService.deleteCar(carId);

        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(carRepository).findById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue(), "Car ID should match");

        verify(carRepository, Mockito.never()).deleteById(carId);
        assertEquals("Car not found", result, "Should return 'Car not found'");
    }

    static List<Arguments> сornerCaseProvider() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(1));
        args.add(Arguments.of(2));
        return args;
    }

    @ParameterizedTest
    @ValueSource(ints = { 1 })
    public void testCornerCase(int carId) {
        Car car = new Car();
        car.setId(carId);
        car.setBrand("Toyota");
        car.setType(CarType.SEDAN);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        String deleteResult = carService.deleteCar(carId);
        assertEquals("Car deleted", deleteResult, "Should return 'Car deleted'");
        verify(carRepository).deleteById(carId);

        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        String secondDeleteResult = carService.deleteCar(carId);
        assertEquals("Car not found", secondDeleteResult, "Should return 'Car not found'");

        verify(carRepository, Mockito.times(1)).deleteById(carId);
    }
}
