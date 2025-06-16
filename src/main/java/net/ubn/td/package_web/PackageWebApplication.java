package net.ubn.td.package_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.ubn.td.package_web.config.PackagerProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(PackagerProperties.class)
public class PackageWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PackageWebApplication.class, args);
	}

}
