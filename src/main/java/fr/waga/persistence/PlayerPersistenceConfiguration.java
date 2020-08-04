package fr.waga.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EntityScan(basePackageClasses = PlayerPersistenceConfiguration.class)
@EnableJpaRepositories(basePackageClasses = PlayerPersistenceConfiguration.class)
@Configuration
public class PlayerPersistenceConfiguration {

}
