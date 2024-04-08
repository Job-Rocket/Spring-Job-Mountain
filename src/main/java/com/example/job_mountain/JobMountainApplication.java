package com.example.job_mountain;

import com.example.job_mountain.config.AppProperties;
import com.example.job_mountain.config.StorageProperties; // StorageProperties 임포트
import com.example.job_mountain.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties({AppProperties.class, StorageProperties.class}) // 여기 수정
@SpringBootApplication
@EnableCaching
public class JobMountainApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobMountainApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
