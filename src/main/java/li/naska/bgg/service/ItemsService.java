package li.naska.bgg.service;

import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.search.Results;
import li.naska.bgg.mapper.HotItemsParamsMapper;
import li.naska.bgg.mapper.SearchParamsMapper;
import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.model.BggHotItemsQueryParams;
import li.naska.bgg.repository.model.BggSearchQueryParams;
import li.naska.bgg.resource.v3.model.HotItemsParams;
import li.naska.bgg.resource.v3.model.SearchParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ItemsService {

  @Autowired
  private BggHotItemsRepository hotItemsRepository;

  @Autowired
  private HotItemsParamsMapper hotItemsParamsMapper;

  @Autowired
  private BggSearchRepository searchRepository;

  @Autowired
  private SearchParamsMapper searchParamsMapper;


  public Mono<HotItems> getHotItems(HotItemsParams params) {
    BggHotItemsQueryParams bggParams = hotItemsParamsMapper.toBggModel(params);
    return hotItemsRepository.getHotItems(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(HotItems.class));
  }

  public Mono<Results> searchItems(SearchParams params) {
    BggSearchQueryParams bggParams = searchParamsMapper.toBggModel(params);
    return searchRepository.getResults(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Results.class));
  }

}
