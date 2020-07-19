package li.naska.bgg.exception;

public class BggBadRequestError extends RuntimeException {

  private String message;

  public BggBadRequestError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
