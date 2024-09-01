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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCarTest {
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(carRepository, null);
    }

    static List<Arguments> providerPositiveTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of("Toyota", CarType.SEDAN, LocalDate.of(2022, 8, 30), new Person("John Doe", PersonStatus.DEACTIVATED, "1234")));
        args.add(Arguments.of("Honda", CarType.COUPE, LocalDate.of(2023, 1, 15), new Person("Jane Doe", PersonStatus.DEACTIVATED, "1234")));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(String brand, CarType type, Person person) {
        Car car = carService.createCar(person, type, brand);

        assertNotNull(car, "Car object should be created");
        assertEquals(brand, car.getBrand(), "Brand should match");
        assertEquals(type, car.getType(), "Type should match");
        assertEquals(LocalDate.now(), car.getReleaseDate(), "Release date should be today");
        assertEquals(person, car.getPerson(), "Person should match");
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(null, CarType.COUPE, new Person("John Doe", PersonStatus.DEACTIVATED, "1234"))); // null brand
        args.add(Arguments.of("", CarType.SEDAN, new Person("Jane Doe", PersonStatus.DEACTIVATED, "1234"))); // empty brand
        args.add(Arguments.of("Ford", null, new Person("John Doe", PersonStatus.DEACTIVATED, "1234"))); // null type
        args.add(Arguments.of("Chevrolet", CarType.PICKUP, null));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void negativeTest(String brand, CarType type, Person person) {
        assertThrows(IllegalArgumentException.class, () -> {
            carService.createCar(person, type, brand);
        });
    }

    static List<Arguments> providerCornerCaseTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of("   Toyota   ", CarType.SEDAN, new Person("John Doe", PersonStatus.DEACTIVATED, "1234")));
        args.add(Arguments.of("Toyota", CarType.SEDAN, new Person("Alice Smith", PersonStatus.DEACTIVATED, "9101")));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerCornerCaseTest")
    public void cornerCaseTest(String brand, CarType type, Person person) {
        Car car = carService.createCar(person, type, brand);

        assertNotNull(car, "Car object should be created");

        assertEquals(brand.trim(), car.getBrand(), "Brand should match after trimming");
        assertEquals(type, car.getType(), "Type should match");
        assertEquals(person, car.getPerson(), "Person should match");
    }
}
