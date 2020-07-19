package li.naska.bgg.exception;

public class BggAuthenticationRequiredError extends RuntimeException {

  private String message;

  public BggAuthenticationRequiredError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
