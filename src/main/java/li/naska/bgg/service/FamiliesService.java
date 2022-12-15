package li.naska.bgg.service;

import com.boardgamegeek.family.Items;
import li.naska.bgg.mapper.FamiliesParamsMapper;
import li.naska.bgg.mapper.FamilyMapper;
import li.naska.bgg.repository.BggFamilyV2Repository;
import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
import li.naska.bgg.resource.vN.model.FamiliesParams;
import li.naska.bgg.resource.vN.model.Family;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamiliesService {

  @Autowired
  private FamiliesParamsMapper familiesParamsMapper;

  @Autowired
  private BggFamilyV2Repository familiesRepository;

  @Autowired
  private FamilyMapper familyMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<List<Family>> getFamilies(FamiliesParams params) {
    BggFamilyV2QueryParams queryParams = familiesParamsMapper.toBggModel(params);
    return familiesRepository.getFamilies(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class))
        .map(Items::getItems)
        .map(l -> l.stream()
            .map(familyMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<Family> getFamily(Integer id) {
    BggFamilyV2QueryParams queryParams = new BggFamilyV2QueryParams();
    queryParams.setId(id.toString());
    return familiesRepository.getFamilies(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class))
        .map(Items::getItems)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(familyMapper::fromBggModel);
  }

}
