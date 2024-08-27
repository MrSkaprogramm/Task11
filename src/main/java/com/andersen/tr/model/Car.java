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
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private CarType type;
    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Car() {}

    public Car(int id, String brand, CarType type) {
        this.id = id;
        this.brand = brand;
        this.type = type;
    }

    public Car(String brand, CarType type, LocalDate releaseDate, int id) {
        this.brand = brand;
        this.type = type;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public Car(int id, String brand, CarType type, LocalDate releaseDate, Person person) {
        this.id = id;
        this.brand = brand;
        this.type = type;
        this.releaseDate = releaseDate;
        this.person = person;
    }
}
