package li.naska.bgg.resource.v1;

import com.boardgamegeek.company.v1.Companies;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggCompanyV1Repository;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController("CompanyV1Resource")
@RequestMapping("/api/v1/company")
public class CompanyResource {

  @Autowired
  private BggCompanyV1Repository companyRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(value = "/{ids}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Retrieve information about a particular company or companies",
      description = """
          Retrieve information about a particular company or companies.
          <p>
          <i>Syntax</i> : /company/{ids}
          <p>
          <i>Example</i> : /company/17
          <p>
          <i>Example</i> : /company/2,10162
          """
  )
  public Mono<String> getCompanies(@NotNull @PathVariable @Parameter(description = "company id(s)", example = "[ 17, 10162 ]") Set<Integer> ids,
                                   ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return companyRepository.getCompanies(ids)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Companies.class));
  }

}
