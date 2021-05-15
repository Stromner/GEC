package gec;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class GECLauncher {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(GECLauncher.class);
        builder.headless(false);
        builder.run(args);
    }
}
