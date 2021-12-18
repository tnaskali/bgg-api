package li.naska.bgg.service;

import com.boardgamegeek.forum.Forum;
import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.mapper.ForumListsParamsMapper;
import li.naska.bgg.mapper.ForumsParamsMapper;
import li.naska.bgg.repository.BggForumListsRepository;
import li.naska.bgg.repository.BggForumsRepository;
import li.naska.bgg.repository.model.BggForumQueryParams;
import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.resource.v3.model.ForumParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ForumsService {

  @Autowired
  private BggForumsRepository forumsRepository;

  @Autowired
  private ForumsParamsMapper forumsParamsMapper;

  @Autowired
  private BggForumListsRepository forumListsRepository;

  @Autowired
  private ForumListsParamsMapper forumListsParamsMapper;

  public Mono<Forum> getForum(ForumParams params) {
    BggForumQueryParams bggParams = forumsParamsMapper.toBggModel(params);
    return forumsRepository.getForum(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Forum.class));
  }

  public Mono<Forums> getForums(ForumsParams params) {
    BggForumsQueryParams bggParams = forumListsParamsMapper.toBggModel(params);
    return forumListsRepository.getForums(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Forums.class));
  }

}
