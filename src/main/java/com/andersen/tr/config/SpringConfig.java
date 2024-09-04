package com.andersen.tr.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.repository.PersonRepository;
import com.andersen.tr.repository.TicketDataRepository;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import com.andersen.tr.service.impl.TicketDataService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.andersen.tr.repository", "com.andersen.tr.model", "com.andersen.tr.service", "com.andersen.tr"})
@PropertySource("classpath:application.properties")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.andersen.tr.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef="transactionManager")
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
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource()); // обновленная строка
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("com.andersen.tr.model");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        factory.setJpaProperties(jpaProperties);
        return factory;
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
    public PersonService personService(PersonRepository personRepository, CarRepository carRepository, String conditionalBean) {
        return new PersonService(isUpdateEnabled, carService(carRepository, conditionalBean), personRepository, carRepository);
    }

    @Bean
    public CarService carService(CarRepository carRepository, String conditionalBean) {
        return new CarService(carRepository, conditionalBean);
    }

    @Bean
    public TicketDataService ticketDataService(TicketDataRepository ticketDataRepository) {
        return new TicketDataService(ticketDataRepository);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory().getObject());
        return tm;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("hey")
                .password("1234")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }
}
