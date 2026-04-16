package com.brofurs.brofurs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * WebMvcConfig
 *
 * Registers resource handlers so Spring Boot serves: - /uploads/** → files
 * uploaded by the admin (product images) - /css/** → static stylesheets -
 * /js/** → static JavaScript files - /images/** → static images bundled with
 * the application
 *
 * The uploads directory path is configurable via application.properties:
 * app.upload.dir=uploads (relative, resolves to project root)
 * app.upload.dir=/var/furnicraft/uploads (absolute path for production)
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${app.upload.dir:uploads}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// Serve uploaded product images from the filesystem
		String uploadLocation = "file:" + Paths.get(uploadDir).toAbsolutePath().normalize() + "/";
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadLocation);

		// Serve static CSS from classpath
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

		// Serve static JavaScript from classpath
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

		// Serve static images (logo, placeholders, etc.) from classpath
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
	}
}