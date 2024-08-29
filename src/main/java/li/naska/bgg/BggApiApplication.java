package li.naska.bgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BggApiApplication {

  public static void main(String[] args) {
    // override Netty HttpClient default behaviour (no max idle time) because connections get closed
    // by BGG after some time
    System.setProperty("reactor.netty.pool.maxIdleTime", "60000");
    SpringApplication.run(BggApiApplication.class, args);
  }
}
