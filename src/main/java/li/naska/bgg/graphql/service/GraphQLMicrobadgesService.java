package li.naska.bgg.graphql.service;

import li.naska.bgg.repository.BggMicrobadgesV4Repository;
import li.naska.bgg.repository.model.BggMicrobadgesV4ResponseBody;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GraphQLMicrobadgesService {

  private final BggMicrobadgesV4Repository microbadgesRepository;

  public GraphQLMicrobadgesService(BggMicrobadgesV4Repository microbadgesRepository) {
    this.microbadgesRepository = microbadgesRepository;
  }

  public Mono<BggMicrobadgesV4ResponseBody> getMicrobadge(Integer id) {
    return microbadgesRepository.getMicrobadge(id);
  }
}
