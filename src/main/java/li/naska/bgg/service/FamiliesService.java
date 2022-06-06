package li.naska.bgg.service;

import com.boardgamegeek.family.Families;
import li.naska.bgg.mapper.FamiliesParamsMapper;
import li.naska.bgg.mapper.FamilyMapper;
import li.naska.bgg.repository.BggFamiliesRepository;
import li.naska.bgg.repository.model.BggFamiliesQueryParams;
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
  private BggFamiliesRepository familiesRepository;

  @Autowired
  private FamilyMapper familyMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<List<Family>> getFamilies(FamiliesParams params) {
    BggFamiliesQueryParams queryParams = familiesParamsMapper.toBggModel(params);
    return familiesRepository.getFamilies(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Families.class))
        .map(Families::getItem)
        .map(l -> l.stream()
            .map(familyMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<Family> getFamily(Integer id) {
    BggFamiliesQueryParams queryParams = new BggFamiliesQueryParams();
    queryParams.setId(id.toString());
    return familiesRepository.getFamilies(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Families.class))
        .map(Families::getItem)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(familyMapper::fromBggModel);
  }

}
