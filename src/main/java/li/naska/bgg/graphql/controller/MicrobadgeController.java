package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.MicrobadgeV4;
import li.naska.bgg.graphql.model.Microbadge;
import li.naska.bgg.repository.BggMicrobadgesV4Repository;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Controller("GraphQLMicrobadgeController")
public class MicrobadgeController {

  public MicrobadgeController(BatchLoaderRegistry registry, BggMicrobadgesV4Repository microbadgesService) {
    registry.forTypePair(Integer.class, MicrobadgeV4.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(microbadgesService.getMicrobadge(id)))
            .collectMap(Tuple2::getT1, tuple -> new MicrobadgeV4(tuple.getT2()))
    );
  }

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
