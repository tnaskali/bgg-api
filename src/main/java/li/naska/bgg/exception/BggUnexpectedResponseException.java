package li.naska.bgg.exception;

public class BggUnexpectedResponseException extends RuntimeException {

  private String message;

  public BggUnexpectedResponseException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
