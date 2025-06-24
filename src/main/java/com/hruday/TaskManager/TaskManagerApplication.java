package com.hruday.TaskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);

		Runtime rt = Runtime.getRuntime();

		int numProc = Runtime.getRuntime().availableProcessors();

		long total_mem = rt.totalMemory();

		long free_mem = rt.freeMemory();

		long used_mem = total_mem - free_mem;

		System.out.println("Amount of used memory: " + used_mem);
		System.out.println("Number of processors: " + numProc);


	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}
