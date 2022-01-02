package li.naska.bgg.service;

import com.boardgamegeek.enums.ItemType;
import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.mapper.*;
import li.naska.bgg.repository.BggForumListsRepository;
import li.naska.bgg.repository.BggForumsRepository;
import li.naska.bgg.repository.BggThreadsRepository;
import li.naska.bgg.repository.model.BggForumQueryParams;
import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.repository.model.BggThreadQueryParams;
import li.naska.bgg.resource.v3.model.Forum;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.resource.v3.model.Thread;
import li.naska.bgg.resource.v3.model.ThreadParams;
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
  private BggForumsRepository forumsRepository;

  @Autowired
  private ForumParamsMapper forumParamsMapper;

  @Autowired
  private ForumMapper forumMapper;

  @Autowired
  private BggForumListsRepository forumListsRepository;

  @Autowired
  private ForumListsParamsMapper forumListsParamsMapper;

  @Autowired
  private BggThreadsRepository threadsRepository;

  @Autowired
  private ThreadParamsMapper threadParamsMapper;

  @Autowired
  private ThreadMapper threadMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Forum> getForum(Integer id) {
    BggForumQueryParams queryParams = new BggForumQueryParams();
    queryParams.setId(id);
    return getForum(queryParams);
  }

  public Mono<List<Forum>> getForums(ForumsParams params) {
    BggForumsQueryParams queryParams = forumListsParamsMapper.toBggModel(params);
    return forumListsRepository.getForums(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Forums.class))
        .map(f -> f.getForum().stream()
            .map(forumMapper::fromBggModel)
            .collect(Collectors.toList()));
  }

  public Mono<List<Forum>> getThingForums(Integer id) {
    ForumsParams parameters = new ForumsParams();
    parameters.setId(id);
    parameters.setType(ItemType.thing);
    return getForums(parameters);
  }

  public Mono<List<Forum>> getFamilyForums(Integer id) {
    ForumsParams parameters = new ForumsParams();
    parameters.setId(id);
    parameters.setType(ItemType.family);
    return getForums(parameters);
  }

  public Mono<Thread> getThread(Integer id, ThreadParams params) {
    BggThreadQueryParams queryParams = threadParamsMapper.toBggModel(params);
    queryParams.setId(id);
    return threadsRepository.getThread(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.thread.Thread.class))
        .map(threadMapper::fromBggModel);
  }

  public Mono<List<Thread>> getThreads(Integer id) {
    BggForumQueryParams firstPageQueryParams = new BggForumQueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setPage(1);
    return getForum(firstPageQueryParams)
        .flatMap(forum -> {
          int numPages = (int) Math.ceil((double) forum.getNumthreads() / BGG_FORUM_THREADS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMap(page -> {
                if (page == 1) {
                  return Mono.just(forum);
                }
                BggForumQueryParams queryParams = new BggForumQueryParams();
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
    BggForumQueryParams firstPageQueryParams = new BggForumQueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setPage(1);
    return getForum(firstPageQueryParams)
        .flatMap(guild -> Flux.range(helper.getBggStartPage(), helper.getBggPages())
            .flatMap(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(guild);
              }
              BggForumQueryParams queryParams = new BggForumQueryParams();
              queryParams.setId(id);
              queryParams.setPage(page);
              return getForum(queryParams);
            })
            .flatMapIterable(Forum::getThreads)
            .collect(Collectors.toList())
            .map(list -> Page.of(
                pagingParams.getPage(),
                (int) Math.ceil((double) guild.getNumthreads() / pagingParams.getSize()),
                pagingParams.getSize(),
                guild.getNumthreads(),
                list.subList(
                    Math.min(
                        helper.getResultStartIndex(),
                        list.size()),
                    Math.min(
                        helper.getResultEndIndex(),
                        list.size())))
            ));
  }

  private Mono<Forum> getForum(BggForumQueryParams queryParams) {
    return forumsRepository.getForum(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.forum.Forum.class))
        .map(forumMapper::fromBggModel);
  }

}
