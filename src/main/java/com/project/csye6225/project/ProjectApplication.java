package com.project.csye6225.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
	"com.project.csye6225.project.controller", 
	"com.project.csye6225.project.pojo",
  	"com.project.csye6225.project.repository",
	"com.project.csye6225.project.service"})
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
