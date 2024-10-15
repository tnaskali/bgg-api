package li.naska.bgg.repository.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class BggGeekpollV3ResponseBody {
  private Poll poll;
  private List<PollQuestion> pollquestions;

  @Data
  public static class Poll {
    private Integer pollid;
    private Integer userid;
    private String title;
    private String body;
    private String polltype;
    private Integer parentpoll;
    private Integer submitted;
    private Integer continuous;
    private Integer hideanswers;
    private LocalDateTime postdate;
    private LocalDateTime editdate;
    private LocalDateTime enddatetime;
    private Integer voters;
    private Integer unixenddatetime;
  }

  @Data
  public static class PollQuestion {
    private Integer questionid;
    private String questiontype;
    private Integer questionnum;
    private Integer pollid;
    private String questionformat;
    private Results results;
  }

  @Data
  public static class Results {
    private Integer questionid;
    private String questiontype;
    private String questionnum;
    private Integer pollid;
    private String questionformat;
    private Question question;
    private Integer userid;
    private Map<Integer, Integer> rowvoters;
    private Integer voters;
    private List<Result> results;
  }

  @Data
  public static class Question {
    private Integer questionid;
    private Integer pollid;
    private String questiontype;
    private String body;
    private Integer questionnum;
    private List<ChoiceColumn> choicesc;
    private List<ChoiceRow> choicesr;
    private Integer colcount;
  }

  @Data
  public static class ChoiceColumn {
    private Integer choiceid;
    private Integer choicenum;
    private String body;
    private String choicetype;
  }

  @Data
  public static class ChoiceRow {
    private Integer choiceid;
    private Integer choicenum;
    private String body;
    private String choicetype;
    private Integer voters;
  }

  @Data
  public static class Result {
    private Integer votes;
    private Integer choiceidcolumn;
    private Integer choiceidrow;
    private String columnbody;
    private String rowbody;
    private BigDecimal percent;
    private BigDecimal percenttrunk;
    private Boolean max;
    private Boolean maxrows;
  }
}
