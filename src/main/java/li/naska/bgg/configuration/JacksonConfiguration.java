package li.naska.bgg.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import li.naska.bgg.util.SafeLocalDateJacksonDeserializer;
import li.naska.bgg.util.SafeLocalDateTimeJacksonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Configuration
public class JacksonConfiguration {

  @Bean
  public ObjectMapper standardObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule javaTimeModule = new JavaTimeModule();
    // deserialize bad formatted dates to null
    javaTimeModule.addDeserializer(LocalDateTime.class, SafeLocalDateTimeJacksonDeserializer.INSTANCE);
    javaTimeModule.addDeserializer(LocalDate.class, SafeLocalDateJacksonDeserializer.INSTANCE); // see BggGeekitemV4ResponseBody#commercelinks
    mapper.registerModule(javaTimeModule);
    mapper.setDefaultPropertyInclusion(NON_EMPTY);
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT); // see BggUserV4ResponseBody#adminBadges
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

}
