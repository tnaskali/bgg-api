package li.naska.bgg.service;

import li.naska.bgg.mapper.ThreadMapper;
import li.naska.bgg.mapper.ThreadParamsMapper;
import li.naska.bgg.repository.BggThreadsRepository;
import li.naska.bgg.repository.model.BggThreadQueryParams;
import li.naska.bgg.resource.v3.model.Thread;
import li.naska.bgg.resource.v3.model.ThreadParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ThreadsService {

  @Autowired
  private BggThreadsRepository threadsRepository;

  @Autowired
  private ThreadParamsMapper threadParamsMapper;

  @Autowired
  private ThreadMapper threadMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Thread> getThread(Integer id, ThreadParams params) {
    BggThreadQueryParams bggParams = threadParamsMapper.toBggModel(params);
    bggParams.setId(id);
    return threadsRepository.getThread(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.thread.Thread.class))
        .map(threadMapper::fromBggModel);
  }

}
