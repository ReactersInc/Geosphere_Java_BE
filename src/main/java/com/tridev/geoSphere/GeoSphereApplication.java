package com.tridev.geoSphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeoSphereApplication {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("✅ MySQL JDBC Driver is loaded!");

		} catch (ClassNotFoundException e) {
			System.out.println("❌ MySQL JDBC Driver NOT found. Please check your dependencies.");
		}

		SpringApplication.run(GeoSphereApplication.class, args);
	}

}
