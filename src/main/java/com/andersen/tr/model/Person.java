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
    @Column(name = "status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PersonStatus status;
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Car> cars = new ArrayList<>();

    public Person() {
    }

    public Person(String name, PersonStatus status, String password) {
        this.name = name;
        this.status = status;
        this.password = password;
    }

    public Person(int id, String name, PersonStatus status, String password) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.password = password;
    }
}
