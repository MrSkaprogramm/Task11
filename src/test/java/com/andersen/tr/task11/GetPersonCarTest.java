package com.andersen.tr.task11;

import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.PersonStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetPersonCarTest {
    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarService(carRepository, "Test conditional bean");
    }

    static List<Arguments> providerPositiveTest() {
        List<Arguments> args = new ArrayList<>();
        Person person = new Person("Alice", PersonStatus.DEACTIVATED, "password");

        Car car = new Car();
        car.setId(1);
        car.setBrand("Toyota");
        car.setType(CarType.PICKUP);
        person.setCars(new ArrayList<>());
        person.getCars().add(car);

        args.add(Arguments.of(person));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(Person person) {
        Car retrievedCar = carService.getPersonCar(person);

        Car expectedCar = person.getCars().get(0);
        assertEquals(expectedCar.getId(), retrievedCar.getId());
        assertEquals(expectedCar.getBrand(), retrievedCar.getBrand());
        assertEquals(expectedCar.getType(), retrievedCar.getType());
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();
        Person person = new Person("Alice", PersonStatus.DEACTIVATED, "password");
        person.setCars(new ArrayList<>());
        args.add(Arguments.of(person));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void negativeTest(Person person) {
        assertThrows(IllegalArgumentException.class, () -> {
            carService.getPersonCar(person);
        });

        Mockito.verify(carRepository, Mockito.never()).findById(Mockito.anyInt());
    }

    static List<Arguments> providerCornerCaseTest() {
        List<Arguments> args = new ArrayList<>();

        Person person = new Person("Frank", PersonStatus.ACTIVATED, "password");
        Car car = new Car();
        car.setId(1);
        car.setBrand("  Ford  ");
        car.setType(CarType.SEDAN);
        person.setCars(List.of(car));

        args.add(Arguments.of(person));

        return args;
    }

    @ParameterizedTest
    @MethodSource("providerCornerCaseTest")
    public void cornerCaseTest(Person person) {
        Car retrievedCar = carService.getPersonCar(person);
        Car expectedCar = person.getCars().get(0);
        assertEquals(expectedCar.getId(), retrievedCar.getId());
        assertEquals(expectedCar.getBrand().trim(), retrievedCar.getBrand().trim());
        assertEquals(expectedCar.getType(), retrievedCar.getType());

        Mockito.verify(carRepository, Mockito.never()).findById(Mockito.anyInt());
    }
}
