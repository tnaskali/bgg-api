package li.naska.bgg.service;

import li.naska.bgg.mapper.UserMapper;
import li.naska.bgg.mapper.UserParamsMapper;
import li.naska.bgg.repository.BggUsersRepository;
import li.naska.bgg.repository.model.BggUserQueryParams;
import li.naska.bgg.resource.v3.model.User;
import li.naska.bgg.resource.v3.model.UserParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsersService {

  @Autowired
  private BggUsersRepository usersRepository;

  @Autowired
  private UserParamsMapper userParamsMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<User> getUser(String username, UserParams params) {
    BggUserQueryParams bggParams = userParamsMapper.toBggModel(params);
    bggParams.setName(username);
    return usersRepository.getUser(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.user.User.class))
        .map(userMapper::fromBggModel);
  }

}
