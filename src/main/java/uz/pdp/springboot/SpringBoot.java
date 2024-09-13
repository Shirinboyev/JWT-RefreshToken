package uz.pdp.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import uz.pdp.springboot.utils.SecurityUtils;

import javax.sql.DataSource;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBoot {

    private final SecurityUtils securityUtils;

    public SpringBoot(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot.class, args);
    }
   //    @Bean
    public CommandLineRunner insertQuery(DataSource dataSource) {
        return args -> {
            ClassPathResource resource = new ClassPathResource("query.sql");
            ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
        };
    }

    @Bean
    public AuditorAware<Integer> auditorAware(){
        return ()->{
            return Optional.ofNullable(securityUtils.getUser());
        };
    };


}
