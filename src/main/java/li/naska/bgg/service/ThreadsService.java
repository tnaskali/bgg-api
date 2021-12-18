package li.naska.bgg.service;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.mapper.ThreadParamsMapper;
import li.naska.bgg.repository.BggThreadsRepository;
import li.naska.bgg.repository.model.BggThreadQueryParams;
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

  public Mono<Thread> getThread(ThreadParams params) {
    BggThreadQueryParams bggParams = threadParamsMapper.toBggModel(params);
    return threadsRepository.getThread(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Thread.class));
  }

}
