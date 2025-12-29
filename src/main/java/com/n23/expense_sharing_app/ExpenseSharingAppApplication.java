package com.n23.expense_sharing_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // <--- ADD THIS LINE
@EnableCaching
public class ExpenseSharingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseSharingAppApplication.class, args);
    }

}
