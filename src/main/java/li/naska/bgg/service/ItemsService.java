package li.naska.bgg.service;

import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.search.Results;
import li.naska.bgg.mapper.HotItemsMapper;
import li.naska.bgg.mapper.HotItemsParamsMapper;
import li.naska.bgg.mapper.SearchParamsMapper;
import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.model.BggHotItemsQueryParams;
import li.naska.bgg.repository.model.BggSearchQueryParams;
import li.naska.bgg.resource.v3.model.HotItem;
import li.naska.bgg.resource.v3.model.HotItemsParams;
import li.naska.bgg.resource.v3.model.SearchParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsService {

  @Autowired
  private BggHotItemsRepository hotItemsRepository;

  @Autowired
  private HotItemsParamsMapper hotItemsParamsMapper;

  @Autowired
  private HotItemsMapper hotItemsMapper;

  @Autowired
  private BggSearchRepository searchRepository;

  @Autowired
  private SearchParamsMapper searchParamsMapper;


  public Mono<List<HotItem>> getHotItems(HotItemsParams params) {
    BggHotItemsQueryParams bggParams = hotItemsParamsMapper.toBggModel(params);
    return hotItemsRepository.getHotItems(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(HotItems.class))
        .map(HotItems::getItem)
        .map(e -> e.stream()
            .map(hotItemsMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<Results> searchItems(SearchParams params) {
    BggSearchQueryParams bggParams = searchParamsMapper.toBggModel(params);
    return searchRepository.getResults(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Results.class));
  }

}
