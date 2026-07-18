package bg.softuni.pizza_delivery_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class PizzaDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaDeliveryApplication.class, args);
	}
}