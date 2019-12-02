package org.me.utm.config;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages ="org.me.utm.repository",entityManagerFactoryRef = "H2EntityManager", 
transactionManagerRef = "H2TransactionManager")
@EnableTransactionManagement
public class DatabaseConfig {
	
	public DataSource dataSourceH2() throws SQLException {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setUser("admin");
		dataSource.setPassword("changeit");
		//change url to the "test" db file location
		dataSource.setURL("jdbc:h2:~/restfulws;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE");
		//dataSource.setImplicitCachingEnabled(true);
		// dataSource.setFastConnectionFailoverEnabled(true);
		System.out.println("***** Created dataSource ******");
		return dataSource;
	}

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TCPServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers");
    }
    
    @Bean
	public LocalContainerEntityManagerFactoryBean H2EntityManager() throws SQLException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSourceH2());
		em.setPackagesToScan(new String[] { "org.me.utm.model" });
		em.setPersistenceUnitName("H2Unit");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.me.utm.model.util.FixedH2Dialect");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.generate-ddl", "false");
		properties.put("hibernate.database", "H2");
		em.setJpaPropertyMap(properties);
		em.afterPropertiesSet();
		return em;
	}
    
	@Bean
	public PlatformTransactionManager H2TransactionManager() throws SQLException {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(H2EntityManager().getObject());
		return transactionManager;
	}

}
