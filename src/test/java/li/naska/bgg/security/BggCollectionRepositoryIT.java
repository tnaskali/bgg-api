package li.naska.bgg.security;

import li.naska.bgg.repository.BggCollectionRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
public class BggCollectionRepositoryIT {

  private WebTestClient testClient = WebTestClient
      .bindToServer()
      .baseUrl("http://localhost:8088")
      .build();
  ;

  private BggCollectionRepository repository;

}
