package org.fsq.blogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.fsq.blogapi.data")
public class BlogapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogapiApplication.class, args);
	}

}
