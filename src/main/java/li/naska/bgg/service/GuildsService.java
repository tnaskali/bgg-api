package li.naska.bgg.service;

import li.naska.bgg.mapper.GuildMapper;
import li.naska.bgg.mapper.GuildMembersParamsMapper;
import li.naska.bgg.repository.BggGuildV2Repository;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
import li.naska.bgg.resource.vN.model.Guild;
import li.naska.bgg.resource.vN.model.Guild.Member;
import li.naska.bgg.resource.vN.model.GuildMembersParams;
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
  private BggGuildV2Repository guildsRepository;

  @Autowired
  private GuildMembersParamsMapper guildMembersParamsMapper;

  @Autowired
  private GuildMapper guildMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Guild> getGuild(Integer id) {
    BggGuildV2QueryParams queryParams = new BggGuildV2QueryParams();
    queryParams.setId(id);
    return getGuild(queryParams);
  }

  private Mono<Guild> getGuild(BggGuildV2QueryParams queryParams) {
    return guildsRepository.getGuild(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.guild.v2.Guild.class))
        .map(guildMapper::fromBggModel);
  }

  public Mono<List<Member>> getMembers(Integer id, GuildMembersParams params) {
    BggGuildV2QueryParams firstPageQueryParams = guildMembersParamsMapper.toBggModel(params);
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setMembers(1);
    firstPageQueryParams.setPage(1);
    return getGuild(firstPageQueryParams)
        .flatMap(guild -> {
          int numPages = (int) Math.ceil((double) guild.getNummembers() / BGG_GUILD_MEMBERS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(guild);
                }
                BggGuildV2QueryParams queryParams = guildMembersParamsMapper.toBggModel(params);
                queryParams.setId(id);
                queryParams.setMembers(1);
                queryParams.setPage(page);
                return getGuild(queryParams);
              })
              .flatMapIterable(Guild::getMembers)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Member>> getPagedMembers(Integer id, GuildMembersParams params, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_GUILD_MEMBERS_PAGE_SIZE);
    BggGuildV2QueryParams firstPageQueryParams = guildMembersParamsMapper.toBggModel(params);
    firstPageQueryParams.setId(id);
    firstPageQueryParams.setMembers(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getGuild(firstPageQueryParams)
        .flatMap(guild -> helper.getBggPagesRange(guild.getNummembers())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(guild);
              }
              BggGuildV2QueryParams queryParams = guildMembersParamsMapper.toBggModel(params);
              queryParams.setId(id);
              queryParams.setMembers(1);
              queryParams.setPage(page);
              return getGuild(queryParams);
            })
            .flatMapIterable(Guild::getMembers)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, guild.getNummembers()))
        );
  }

}
