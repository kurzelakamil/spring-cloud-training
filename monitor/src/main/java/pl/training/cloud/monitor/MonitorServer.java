package pl.training.cloud.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class MonitorServer {

    public static void main(String[] args) {
        SpringApplication.run(MonitorServer.class, args);
    }

}
