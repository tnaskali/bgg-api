package li.naska.bgg.resource.v2;

import com.boardgamegeek.company.v2.Items;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggCompanyV2Repository;
import li.naska.bgg.repository.model.BggCompanyV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("CompanyV2Resource")
@RequestMapping("/api/v2/company")
public class CompanyResource {

  @Autowired
  private BggCompanyV2Repository companiesRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Company items",
      description =
          """
          In the BGG database, any commercial entity is called a company.
          <p>
          <i>Syntax</i> : /company?id={ids}[&{parameters}]
          <p>
          <i>Example</i> : /company?id=13129,30347
          """)
  public Mono<String> getCompanies(
      @Validated @ParameterObject BggCompanyV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return companiesRepository
        .getCompanies(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }
}
