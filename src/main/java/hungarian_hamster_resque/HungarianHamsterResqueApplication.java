package hungarian_hamster_resque;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HungarianHamsterResqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(HungarianHamsterResqueApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Hungarian Hamster Resque API")
						.version("1.0.0")
						.description("Operations with hamsters, hosts and adopters"));
	}
}
