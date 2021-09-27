package li.naska.bgg.repository;

import com.boardgamegeek.family.Families;
import li.naska.bgg.repository.model.BggFamiliesParameters;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggFamiliesRepository {

  private final WebClient webClient;

  public BggFamiliesRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.family.read}") String familyReadEndpoint) {
    this.webClient = builder.baseUrl(familyReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractFamiliesParams(BggFamiliesParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    Optional.ofNullable(params.getTypes()).map(QueryParamFunctions.BGG_FAMILY_TYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    return map;
  }

  public Mono<Families> getFamily(BggFamiliesParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractFamiliesParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Families.class);
  }

}
