package li.naska.bgg.service;

import com.boardgamegeek.forum.Forum;
import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.repository.BggForumRepository;
import li.naska.bgg.repository.BggForumlistRepository;
import li.naska.bgg.repository.model.BggForumParameters;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ForumsService {

  @Autowired
  private BggForumRepository forumRepository;

  @Autowired
  private BggForumlistRepository forumlistRepository;

  public Mono<Forum> getForum(BggForumParameters parameters) {
    return forumRepository.getForum(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Forum.class));
  }

  public Mono<Forums> getForums(BggForumsParameters parameters) {
    return forumlistRepository.getForums(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Forums.class));
  }

}
