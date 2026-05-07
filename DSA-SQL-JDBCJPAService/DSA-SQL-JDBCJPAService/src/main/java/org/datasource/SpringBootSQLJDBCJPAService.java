package org.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.logging.Logger;

/*
 * mvn spring-boot:run
 */
@SpringBootApplication
public class SpringBootSQLJDBCJPAService extends SpringBootServletInitializer {

	private static final Logger logger = Logger.getLogger(SpringBootSQLJDBCJPAService.class.getName());

	public static void main(String[] args) {
		logger.info("Loading ... SpringBoot PostgreSQL Data Service with JDBC and JPA ...");
		SpringApplication.run(SpringBootSQLJDBCJPAService.class, args);
	}
}