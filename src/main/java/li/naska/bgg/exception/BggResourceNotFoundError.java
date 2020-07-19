package li.naska.bgg.exception;

public class BggResourceNotFoundError extends RuntimeException {

  private String message;

  public BggResourceNotFoundError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
