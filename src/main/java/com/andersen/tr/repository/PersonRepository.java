package com.andersen.tr.repository;

import com.andersen.tr.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByName(String name);
}
