package jwt.jwt_utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class JwtUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtUtilsApplication.class, args);
	}
}