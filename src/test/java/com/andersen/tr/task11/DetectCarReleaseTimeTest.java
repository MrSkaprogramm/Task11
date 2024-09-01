package com.andersen.tr.task11;

import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.service.impl.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetectCarReleaseTimeTest {
    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    public void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        carService = new CarService(carRepository, null);
    }

    @Test
    public void testDetectCarReleaseTime() {
        LocalDate actualDate = carService.detectCarReleaseTime();
        LocalDate expectedDate = LocalDate.now();

        assertEquals(expectedDate, actualDate, "The method must return the current date.");
    }
}
