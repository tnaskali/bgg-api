package li.naska.bgg.service;

import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.mapper.ForumListsParamsMapper;
import li.naska.bgg.mapper.ForumMapper;
import li.naska.bgg.mapper.ThreadMapper;
import li.naska.bgg.mapper.ThreadParamsMapper;
import li.naska.bgg.repository.BggForumV2Repository;
import li.naska.bgg.repository.BggForumlistV2Repository;
import li.naska.bgg.repository.BggThreadV2Repository;
import li.naska.bgg.repository.model.BggForumV2QueryParams;
import li.naska.bgg.repository.model.BggForumlistV2QueryParams;
import li.naska.bgg.repository.model.BggThreadV2QueryParams;
import li.naska.bgg.resource.vN.model.Forum;
import li.naska.bgg.resource.vN.model.ForumsParams;
import li.naska.bgg.resource.vN.model.Thread;
import li.naska.bgg.resource.vN.model.ThreadParams;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingHelper;
import li.naska.bgg.util.PagingParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumsService {

  private static final int BGG_FORUM_THREADS_PAGE_SIZE = 50;

  @Autowired
  private BggForumV2Repository forumsRepository;

  @Autowired
  private ForumMapper forumMapper;

  @Autowired
  private BggForumlistV2Repository forumListsRepository;

  @Autowired
  private ForumListsParamsMapper forumListsParamsMapper;

  @Autowired
  private BggThreadV2Repository threadsRepository;

  @Autowired
  private ThreadParamsMapper threadParamsMapper;

  @Autowired
  private ThreadMapper threadMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Forum> getForum(Integer id) {
    BggForumV2QueryParams queryParams = new BggForumV2QueryParams();
    queryParams.setId(id);
    return getForum(queryParams);
  }

  public Mono<List<Forum>> getForums(ForumsParams params) {
    BggForumlistV2QueryParams queryParams = forumListsParamsMapper.toBggModel(params);
    return forumListsRepository.getForums(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Forums.class))
        .map(f -> f.getFora().stream()
            .map(forumMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<List<Forum>> getThingForums(Integer id) {
    ForumsParams params = new ForumsParams();
    params.setId(id);
    params.setType("thing");
    return getForums(params);
  }

  public Mono<List<Forum>> getFamilyForums(Integer id) {
    ForumsParams params = new ForumsParams();
    params.setId(id);
    params.setType("family");
    return getForums(params);
  }

  public Mono<Thread> getThread(Integer id, ThreadParams params) {
    BggThreadV2QueryParams queryParams = threadParamsMapper.toBggModel(params);
    queryParams.setId(id);
    return threadsRepository.getThread(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.thread.Thread.class))
        .map(threadMapper::fromBggModel);
  }

  public Mono<List<Thread>> getThreads(Integer id) {
    BggForumV2QueryParams firstPageQueryParams = new BggForumV2QueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setPage(1);
    return getForum(firstPageQueryParams)
        .flatMap(forum -> {
          int numPages = (int) Math.ceil((double) forum.getNumthreads() / BGG_FORUM_THREADS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(forum);
                }
                BggForumV2QueryParams queryParams = new BggForumV2QueryParams();
                queryParams.setId(id);
                queryParams.setPage(page);
                return getForum(queryParams);
              })
              .flatMapIterable(Forum::getThreads)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Thread>> getPagedThreads(Integer id, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_FORUM_THREADS_PAGE_SIZE);
    BggForumV2QueryParams firstPageQueryParams = new BggForumV2QueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getForum(firstPageQueryParams)
        .flatMap(forum -> helper.getBggPagesRange(forum.getNumthreads())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(forum);
              }
              BggForumV2QueryParams queryParams = new BggForumV2QueryParams();
              queryParams.setId(id);
              queryParams.setPage(page);
              return getForum(queryParams);
            })
            .flatMapIterable(Forum::getThreads)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, forum.getNumthreads()))
        );
  }

  private Mono<Forum> getForum(BggForumV2QueryParams queryParams) {
    return forumsRepository.getForum(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.forum.Forum.class))
        .map(forumMapper::fromBggModel);
  }

}
