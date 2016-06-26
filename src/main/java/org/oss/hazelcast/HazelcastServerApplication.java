package org.oss.hazelcast;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class HazelcastServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastServerApplication.class,args);
	}
}
