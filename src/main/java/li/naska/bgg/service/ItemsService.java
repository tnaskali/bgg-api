package li.naska.bgg.service;

import com.boardgamegeek.hot.v2.Items;
import li.naska.bgg.mapper.HotItemsMapper;
import li.naska.bgg.mapper.HotItemsParamsMapper;
import li.naska.bgg.mapper.ResultsMapper;
import li.naska.bgg.mapper.SearchParamsMapper;
import li.naska.bgg.repository.BggHotV2Repository;
import li.naska.bgg.repository.BggSearchV2Repository;
import li.naska.bgg.repository.model.BggHotV2QueryParams;
import li.naska.bgg.repository.model.BggSearchV2QueryParams;
import li.naska.bgg.resource.vN.model.HotItem;
import li.naska.bgg.resource.vN.model.HotItemsParams;
import li.naska.bgg.resource.vN.model.Results;
import li.naska.bgg.resource.vN.model.SearchParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsService {

  @Autowired
  private BggHotV2Repository hotItemsRepository;

  @Autowired
  private HotItemsParamsMapper hotItemsParamsMapper;

  @Autowired
  private HotItemsMapper hotItemsMapper;

  @Autowired
  private BggSearchV2Repository searchRepository;

  @Autowired
  private SearchParamsMapper searchParamsMapper;

  @Autowired
  private ResultsMapper resultsMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<List<HotItem>> getHotItems(HotItemsParams params) {
    BggHotV2QueryParams queryParams = hotItemsParamsMapper.toBggModel(params);
    return hotItemsRepository.getHotItems(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class))
        .map(Items::getItems)
        .map(e -> e.stream()
            .map(hotItemsMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<Results> searchItems(SearchParams params) {
    BggSearchV2QueryParams queryParams = searchParamsMapper.toBggModel(params);
    return searchRepository.getResults(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.search.v2.Items.class))
        .map(resultsMapper::fromBggModel);
  }

}
