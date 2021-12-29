package li.naska.bgg.service;

import com.boardgamegeek.family.Families;
import li.naska.bgg.mapper.FamiliesParamsMapper;
import li.naska.bgg.mapper.FamilyMapper;
import li.naska.bgg.mapper.FamilyParamsMapper;
import li.naska.bgg.repository.BggFamiliesRepository;
import li.naska.bgg.repository.model.BggFamiliesQueryParams;
import li.naska.bgg.resource.v3.model.FamiliesParams;
import li.naska.bgg.resource.v3.model.Family;
import li.naska.bgg.resource.v3.model.FamilyParams;
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
  private FamilyParamsMapper familyParamsMapper;

  @Autowired
  private BggFamiliesRepository familiesRepository;

  @Autowired
  private FamilyMapper familyMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Family> getFamily(Integer id, FamilyParams params) {
    BggFamiliesQueryParams bggParams = familyParamsMapper.toBggModel(params);
    bggParams.setId(id.toString());
    return familiesRepository.getFamilies(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Families.class))
        .map(Families::getItem)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(familyMapper::fromBggModel);
  }

  public Mono<List<Family>> getFamilies(FamiliesParams params) {
    BggFamiliesQueryParams bggParams = familiesParamsMapper.toBggModel(params);
    return familiesRepository.getFamilies(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Families.class))
        .map(Families::getItem)
        .map(l -> l.stream()
            .map(familyMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

}
