package com.my.app;

import com.epam.app.model.Category;
import com.my.app.model.Product;
import com.my.app.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EnableMongoAuditing
@EnableEurekaClient
@SpringBootApplication
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Bean
    CommandLineRunner runner (ProductRepository repository) {
        return args -> {
            Product product1 = new Product();
            product1.setName("Iphone");
            product1.setCategory(Category.SMARTPHONE);
            product1.setDescription("The best smartphone");
            product1.setCost(BigDecimal.valueOf(1500));
            product1.setHasBluetooth(true);

            Product product2 = new Product();
            product2.setName("IMac");
            product2.setCategory(Category.LAPTOP);
            product2.setDescription("The best laptop");
            product2.setCost(BigDecimal.valueOf(2500));
            product2.setRam("16 Gb");
            product2.setProcessor("Intel Core i7");

            repository.saveAll(List.of(product1, product2));
        };
    }
}
