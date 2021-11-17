package li.naska.bgg.service;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.user.User;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.BggUserRepository;
import li.naska.bgg.repository.model.BggCollectionParameters;
import li.naska.bgg.repository.model.BggUserParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsersService {

  @Autowired
  private BggUserRepository userRepository;

  @Autowired
  private BggCollectionRepository collectionRepository;

  public Mono<User> getUser(BggUserParameters parameters) {
    return userRepository.getUser(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(User.class));
  }

  public Mono<Collection> getCollection(BggCollectionParameters parameters) {
    return collectionRepository.getCollection(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Collection.class));
  }

}
