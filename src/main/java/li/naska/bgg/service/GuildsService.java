package li.naska.bgg.service;

import li.naska.bgg.mapper.GuildMapper;
import li.naska.bgg.mapper.GuildsParamsMapper;
import li.naska.bgg.repository.BggGuildsRepository;
import li.naska.bgg.repository.model.BggGuildQueryParams;
import li.naska.bgg.resource.v3.model.Guild;
import li.naska.bgg.resource.v3.model.Guild.Member;
import li.naska.bgg.resource.v3.model.GuildParams;
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
public class GuildsService {

  private static final int BGG_GUILD_MEMBERS_PAGE_SIZE = 25;

  @Autowired
  private BggGuildsRepository guildsRepository;

  @Autowired
  private GuildsParamsMapper guildsParamsMapper;

  @Autowired
  private GuildMapper guildMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Guild> getGuild(Integer id, GuildParams params) {
    BggGuildQueryParams queryParams = guildsParamsMapper.toBggModel(params);
    queryParams.setId(id);
    return getGuild(queryParams);
  }

  private Mono<Guild> getGuild(BggGuildQueryParams queryParams) {
    return guildsRepository.getGuild(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.guild.Guild.class))
        .map(guildMapper::fromBggModel);
  }

  public Mono<List<Member>> getMembers(Integer id) {
    BggGuildQueryParams firstPageQueryParams = new BggGuildQueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setMembers(1);
    firstPageQueryParams.setPage(1);
    return getGuild(firstPageQueryParams)
        .flatMap(guild -> {
          int numPages = (int) Math.ceil((double) guild.getNummembers() / BGG_GUILD_MEMBERS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMap(page -> {
                if (page == 1) {
                  return Mono.just(guild);
                }
                BggGuildQueryParams queryParams = new BggGuildQueryParams();
                queryParams.setId(id);
                queryParams.setMembers(1);
                queryParams.setPage(page);
                return getGuild(queryParams);
              })
              .flatMapIterable(Guild::getMembers)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Member>> getPagedMembers(Integer id, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_GUILD_MEMBERS_PAGE_SIZE);
    BggGuildQueryParams firstPageQueryParams = new BggGuildQueryParams();
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setMembers(1);
    firstPageQueryParams.setPage(1);
    return getGuild(firstPageQueryParams)
        .flatMap(guild -> Flux.range(helper.getBggStartPage(), helper.getBggPages())
            .flatMap(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(guild);
              }
              BggGuildQueryParams queryParams = new BggGuildQueryParams();
              queryParams.setId(id);
              queryParams.setMembers(1);
              queryParams.setPage(page);
              return getGuild(queryParams);
            })
            .flatMapIterable(Guild::getMembers)
            .collect(Collectors.toList())
            .map(list -> Page.of(
                pagingParams.getPage(),
                (int) Math.ceil((double) guild.getNummembers() / pagingParams.getSize()),
                pagingParams.getSize(),
                guild.getNummembers(),
                list.subList(
                    Math.min(
                        helper.getResultStartIndex(),
                        list.size()),
                    Math.min(
                        helper.getResultEndIndex(),
                        list.size())))
            ));
  }

}
