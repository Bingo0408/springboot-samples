package org.bingo.sample.jdbc.sharding;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class ShardingJDBCSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingJDBCSampleApplication.class, args);
	}

}

