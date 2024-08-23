package com.andersen.tr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"User\"", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "user_status", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Ticket> tickets = new ArrayList<>();

    public User() {
    }

    public User(String name, UserStatus userStatus) {
        this.name = name;
        this.userStatus = userStatus;
    }

    public User(int id, String name, UserStatus userStatus) {
        this.id = id;
        this.name = name;
        this.userStatus = userStatus;
    }
}
