package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.PersonDaoInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao implements PersonDaoInterface {
    private final SessionFactory sessionFactory;

    public PersonDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void savePerson(Person person) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.save(person);
        } catch (Exception e) {
            throw new DaoException("Error saving person", e);
        }
    }

    @Override
    public Person getPersonById(int id) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Person.class, id);
        } catch (Exception e) {
            throw new DaoException("Error fetching person by ID", e);
        }
    }

    @Override
    public void deletePerson(Person person) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.delete(person);
        } catch (Exception e) {
            throw new DaoException("Error deleting person", e);
        }
    }

    @Override
    public void updatePersonAndCar(Person person, Car car) throws DaoException {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(person);
            session.save(car);
            transaction.commit();
        } catch (Exception e) {
            throw new DaoException("Error updating person and car", e);
        }
    }

    @Override
    public boolean checkPersonExist(int userId) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            Person person = session.get(Person.class, userId);
            return person != null;
        } catch (Exception e) {
            throw new DaoException("Error checking if person exists", e);
        }
    }

    @Override
    public void updatePerson(Person person) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.update(person);
        } catch (Exception e) {
            throw new DaoException("Error updating person", e);
        }
    }
}
