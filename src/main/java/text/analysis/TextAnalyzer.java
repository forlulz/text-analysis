package text.analysis;

import java.util.List;
import text.analysis.person.PersonData;

public interface TextAnalyzer {

  public List<PersonData> findPersonInformations(String text);

}
