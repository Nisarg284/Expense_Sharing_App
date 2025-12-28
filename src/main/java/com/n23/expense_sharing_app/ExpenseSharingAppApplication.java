package com.n23.expense_sharing_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // <--- ADD THIS LINE
public class ExpenseSharingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseSharingAppApplication.class, args);
    }

}
