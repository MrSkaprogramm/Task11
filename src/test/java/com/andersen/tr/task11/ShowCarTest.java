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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowCarTest {
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
        args.add(Arguments.of(2, "Honda", CarType.SEDAN));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerPositiveTest")
    public void positiveTest(int carId, String brand, CarType type) {
        Car car = new Car();
        car.setId(carId);
        car.setBrand(brand);
        car.setType(type);
        car.setReleaseDate(LocalDate.now());

        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car foundCar = carService.showCar(carId);
        assertEquals(brand, foundCar.getBrand());
        assertEquals(type, foundCar.getType());
        assertEquals(carId, foundCar.getId());

        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(carRepository).findById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue());
    }

    static List<Arguments> providerNegativeTest() {
        List<Arguments> args = new ArrayList<>();
        args.add(Arguments.of(999));
        args.add(Arguments.of(-1));
        return args;
    }

    @ParameterizedTest
    @MethodSource("providerNegativeTest")
    public void negativeTest(int carId) {
        Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            carService.showCar(carId);
        });

        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(carRepository).findById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue());
    }

    static List<Arguments> providerCornerCaseTest() {
        List<Arguments> args = new ArrayList<>();

        args.add(Arguments.of(0, Optional.empty())); // Невалидный ID (0)
        args.add(Arguments.of(1, Optional.of(new Car("Toyota", CarType.PICKUP, LocalDate.now(), 1))));
        args.add(Arguments.of(Integer.MAX_VALUE, Optional.empty()));

        return args;
    }

    @ParameterizedTest
    @MethodSource("providerCornerCaseTest")
    public void testCornerCaseTest(int carId, Optional<Car> carOptional) {
        Mockito.when(carRepository.findById(carId)).thenReturn(carOptional);

        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            Person person = new Person("Alice", PersonStatus.DEACTIVATED, "password");
            car.setPerson(person);

            Car foundCar = carService.showCar(carId);
            assertEquals(car.getBrand(), foundCar.getBrand());
            assertEquals(car.getType(), foundCar.getType());
        } else {
            assertThrows(IllegalArgumentException.class, () -> {
                carService.showCar(carId);
            });
        }

        ArgumentCaptor<Integer> carIdCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(carRepository).findById(carIdCaptor.capture());
        assertEquals(carId, carIdCaptor.getValue());
    }
}
