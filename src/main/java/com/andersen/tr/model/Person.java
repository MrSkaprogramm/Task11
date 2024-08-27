package com.andersen.tr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"Person\"", schema = "public")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "person_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PersonStatus personStatus;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Car> cars = new ArrayList<>();

    public Person() {
    }

    public Person(String name, PersonStatus personStatus) {
        this.name = name;
        this.personStatus = personStatus;
    }

    public Person(int id, String name, PersonStatus personStatus) {
        this.id = id;
        this.name = name;
        this.personStatus = personStatus;
    }
}
