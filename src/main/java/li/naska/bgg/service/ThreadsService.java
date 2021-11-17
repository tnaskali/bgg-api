package li.naska.bgg.service;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.repository.BggThreadRepository;
import li.naska.bgg.repository.model.BggThreadParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ThreadsService {

  @Autowired
  private BggThreadRepository threadRepository;

  public Mono<Thread> getThread(BggThreadParameters parameters) {
    return threadRepository.getThread(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Thread.class));
  }

}
