package li.naska.bgg.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import li.naska.bgg.exception.BggAuthorizationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class DataFetcherExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable exception, DataFetchingEnvironment env) {
    GraphQLError.Builder<?> errorBuilder = GraphqlErrorBuilder.newError(env);
    if (exception instanceof BggAuthorizationException) {
      errorBuilder.message(
          "Authentication to BoardGameGeek API failed. Check your application token.");
    } else {
      errorBuilder.message(exception.getMessage());
    }
    return errorBuilder.build();
  }
}
