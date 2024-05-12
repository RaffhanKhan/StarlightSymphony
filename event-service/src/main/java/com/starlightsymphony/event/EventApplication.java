package com.starlightsymphony.event;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.WebApplicationInitializer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;

@SpringBootApplication
@RequiredArgsConstructor
@RefreshScope
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.starlightsymphony.event", entityManagerFactoryRef = "starlightsymphonyEntityManager", transactionManagerRef = "platformTransactionManager")
@PropertySource({"classpath:application.properties","classpath:event.properties","classpath:bootstrap.properties"})
public class EventApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	private final Environment env;

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		return builder.sources(EventApplication.class);
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager(){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(starlightsymphonyEntityManager().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean starlightsymphonyEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(starlightsymphonyDataSource());

		em.setPackagesToScan("com.starlightsymphony.event");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> props = new HashMap<>();
		props.put("hibernate.dialect", env.getProperty("starlightsymphony.hibernate.dialect"));
		props.put("hibernate.show_sql", env.getProperty("starlightsymphony.hibernate.show.sql"));
		em.setJpaPropertyMap(props);

		return em;
	}

	@Bean(destroyMethod = "")
	public DataSource starlightsymphonyDataSource(){
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(env.getProperty("starlightsymphony.jdbc.classname"));
		driverManagerDataSource.setUsername(env.getProperty("starlightsymphony.jdbc.username"));
		driverManagerDataSource.setPassword(env.getProperty("starlightsymphony.jdbc.password"));
		driverManagerDataSource.setUrl(env.getProperty("starlightsymphony.jdbc.url"));
		return driverManagerDataSource;
	}

}
