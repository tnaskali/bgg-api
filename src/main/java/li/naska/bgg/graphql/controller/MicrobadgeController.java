package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.MicrobadgeV4;
import li.naska.bgg.graphql.model.Microbadge;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller("GraphQLMicrobadgeController")
public class MicrobadgeController {

  @QueryMapping
  public Mono<Microbadge> microbadgeById(@Argument Integer id, DataLoader<Integer, MicrobadgeV4> loader) {
    return Mono.fromFuture(loader.load(id))
        .map(data -> new Microbadge(data.microbadge().getBadgeid()));
  }

  @SchemaMapping
  public Mono<String> name(Microbadge microbadge, DataLoader<Integer, MicrobadgeV4> loader) {
    return Mono.fromFuture(loader.load(microbadge.id()))
        .map(data -> data.microbadge().getName());
  }

  @SchemaMapping
  public Mono<String> imagesrc(Microbadge microbadge, DataLoader<Integer, MicrobadgeV4> loader) {
    return Mono.fromFuture(loader.load(microbadge.id()))
        .map(data -> data.microbadge().getSrc());
  }

}
