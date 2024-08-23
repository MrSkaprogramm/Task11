package com.andersen.tr.config;

import com.andersen.tr.Main;
import com.andersen.tr.dao.impl.CarDao;
import com.andersen.tr.dao.impl.PersonDao;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.andersen.tr.dao", "com.andersen.tr.model", "com.andersen.tr.service", "com.andersen.tr"})
@PropertySource("classpath:application.properties")
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringConfig {
    @Value("${entity.update.enabled}")
    boolean isUpdateEnabled;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${db.driver}")
    private String connectionDriverClass;

    @Value("${db.url}")
    private String connectionUrl;

    @Value("${db.user}")
    private String connectionUsername;

    @Value("${db.password}")
    private String connectionPassword;

    @Value("${db.poolsize}")
    private int poolSize;

    @Bean
    public PersonService personService() {
        PersonDao personDao = new PersonDao(sessionFactory());
        return new PersonService(isUpdateEnabled, carService(), personDao);
    }

    @Bean
    public CarService carService() {
        CarDao carDao = new CarDao(sessionFactory());
        return new CarService(carDao);
    }

    @Bean
    public Main main(CarService carService, PersonService personService) {
        return new Main(carService, personService);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(connectionDriverClass);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(connectionUsername);
        dataSource.setPassword(connectionPassword);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration.setProperty("hibernate.connection.driver_class", connectionDriverClass);
        configuration.setProperty("hibernate.connection.url", connectionUrl);
        configuration.setProperty("hibernate.connection.username", connectionUsername);
        configuration.setProperty("hibernate.connection.password", connectionPassword);
        configuration.setProperty("hibernate.connection.pool_size", String.valueOf(poolSize));
        configuration.setProperty("hibernate.dialect", hibernateDialect);
        configuration.setProperty("hibernate.show_sql", hibernateShowSql);

        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Car.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }
}
