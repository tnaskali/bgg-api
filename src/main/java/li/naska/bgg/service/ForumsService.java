package li.naska.bgg.service;

import com.boardgamegeek.enums.ItemType;
import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.mapper.ForumListsParamsMapper;
import li.naska.bgg.mapper.ForumMapper;
import li.naska.bgg.mapper.ForumParamsMapper;
import li.naska.bgg.repository.BggForumListsRepository;
import li.naska.bgg.repository.BggForumsRepository;
import li.naska.bgg.repository.model.BggForumQueryParams;
import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.resource.v3.model.Forum;
import li.naska.bgg.resource.v3.model.ForumParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumsService {

  @Autowired
  private BggForumsRepository forumsRepository;

  @Autowired
  private ForumParamsMapper forumParamsMapper;

  @Autowired
  private ForumMapper forumMapper;

  @Autowired
  private BggForumListsRepository forumListsRepository;

  @Autowired
  private ForumListsParamsMapper forumListsParamsMapper;

  public Mono<Forum> getForum(Integer id, ForumParams params) {
    BggForumQueryParams bggParams = forumParamsMapper.toBggModel(params);
    bggParams.setId(id);
    return forumsRepository.getForum(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(com.boardgamegeek.forum.Forum.class))
        .map(e -> forumMapper.fromBggModel(e));
  }

  public Mono<List<Forum>> getForums(ForumsParams params) {
    BggForumsQueryParams bggParams = forumListsParamsMapper.toBggModel(params);
    return forumListsRepository.getForums(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Forums.class))
        .map(f -> f.getForum().stream()
            .map(e -> forumMapper.fromBggModel(e))
            .collect(Collectors.toList()));
  }

  public Mono<List<Forum>> getThingForums(Integer id) {
    ForumsParams parameters = new ForumsParams();
    parameters.setId(id);
    parameters.setType(ItemType.thing);
    return getForums(parameters);
  }

  public Mono<List<Forum>> getFamilyForums(Integer id) {
    ForumsParams parameters = new ForumsParams();
    parameters.setId(id);
    parameters.setType(ItemType.family);
    return getForums(parameters);
  }

}
