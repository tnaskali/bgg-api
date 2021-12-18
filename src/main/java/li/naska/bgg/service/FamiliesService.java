package li.naska.bgg.service;

import com.boardgamegeek.family.Families;
import li.naska.bgg.mapper.FamiliesParamsMapper;
import li.naska.bgg.repository.BggFamiliesRepository;
import li.naska.bgg.repository.model.BggFamiliesQueryParams;
import li.naska.bgg.resource.v3.model.FamiliesParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FamiliesService {

  @Autowired
  private FamiliesParamsMapper familiesParamsMapper;

  @Autowired
  private BggFamiliesRepository familiesRepository;

  public Mono<Families> getFamilies(FamiliesParams params) {
    BggFamiliesQueryParams bggParams = familiesParamsMapper.toBggModel(params);
    return familiesRepository.getFamilies(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Families.class));
  }

}
