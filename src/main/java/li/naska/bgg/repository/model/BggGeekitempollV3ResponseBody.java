package li.naska.bgg.repository.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggGeekitempollV3ResponseBody {

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

    private Question question;

    private String questionformat;

    private String userid;

    private List<Answer> answers;
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
  public static class Answer {

    private Integer voteid;

    private LocalDateTime votedate;

    private Integer choiceidcolumn;

    private Integer choiceidrow;

    private Integer pollid;

    private Integer questionid;

    private Integer userid;
  }
}
