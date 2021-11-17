package li.naska.bgg.service;

import com.boardgamegeek.family.Families;
import li.naska.bgg.repository.BggFamilyRepository;
import li.naska.bgg.repository.model.BggFamilyParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FamiliesService {

  @Autowired
  private BggFamilyRepository familyRepository;

  public Mono<Families> getFamilies(BggFamilyParameters parameters) {
    return familyRepository.getFamily(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Families.class));
  }

}
