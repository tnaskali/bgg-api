package li.naska.bgg.exception;

public class BggResourceNotOwnedError extends RuntimeException {

  private String message;

  public BggResourceNotOwnedError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
