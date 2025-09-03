package sh.admin.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FilloAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilloAdminApplication.class, args);
        System.out.println("======================= [Application Active] =======================");
    }

}
