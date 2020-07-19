package li.naska.bgg.exception;

public class BggNoContentError extends RuntimeException {

  private String message;

  public BggNoContentError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
