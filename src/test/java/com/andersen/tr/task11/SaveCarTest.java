package com.andersen.tr.task11;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.PersonStatus;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaveCarTest {
    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarService(carRepository, "Test conditional bean");
    }

    static List<Arguments> providerPositiveTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of("Toyota", CarType.PICKUP));
        args.add(Arguments.of("Honda", CarType.SEDAN));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(String brand, CarType type) {
        Person person = new Person("Alice", PersonStatus.DEACTIVATED, "password");

        Car car = new Car();
        car.setBrand(brand);
        car.setType(type);
        car.setReleaseDate(carService.detectCarReleaseTime());
        car.setPerson(person);

        Mockito.when(carRepository.save(Mockito.any(Car.class))).thenReturn(car);

        Car savedCar = carService.saveCar(person, type, brand);

        assertEquals(brand, savedCar.getBrand());
        assertEquals(type, savedCar.getType());
        assertEquals(person, savedCar.getPerson());

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        Mockito.verify(carRepository).save(carCaptor.capture());

        assertEquals(brand, carCaptor.getValue().getBrand());
        assertEquals(type, carCaptor.getValue().getType());
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(null, CarType.COUPE, "Brand is null"));
        args.add(Arguments.of("", CarType.PICKUP, "Empty brand"));
        args.add(Arguments.of("Toyota", null, "The type is null"));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void negativeTest(String brand, CarType type) {
        Person person = new Person("Test User", PersonStatus.DEACTIVATED, "password");

        assertThrows(IllegalArgumentException.class, () -> carService.saveCar(person, type, brand));

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
    }

    static List<Arguments> providerCornerCaseTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of("   ", CarType.SEDAN, "A brand consists only of spaces"));
        args.add(Arguments.of("Ford", CarType.COUPE, "Correct data"));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerCornerCaseTest")
    public void cornerCaseTest(String brand, CarType type) {
        Person person = new Person("John Doe", PersonStatus.DEACTIVATED, "password");

        Car car = carService.createCar(person, type, brand);

        assertEquals(brand.trim(), car.getBrand(), "Brand should match after trimming");
        assertEquals(type, car.getType(), "Car type should match");
        assertEquals(person, car.getPerson(), "Person should match");

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        Mockito.verify(carRepository, Mockito.never()).save(carCaptor.capture());
    }
}
