package com.andersen.tr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "\"Car\"", schema = "public")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "car_brand", nullable = false)
    private String carBrand;
    @Column(name = "car_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CarType carType;
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Car() {}

    public Car(String carBrand, CarType carType, LocalDate releaseDate, BigDecimal price, Person person) {
        this.carBrand = carBrand;
        this.carType = carType;
        this.releaseDate = releaseDate;
        this.person = person;
    }

    public Car(int id, String carBrand, CarType carType, LocalDate releaseDate, Person person) {
        this.id = id;
        this.carBrand = carBrand;
        this.carType = carType;
        this.releaseDate = releaseDate;
        this.person = person;
    }
}
