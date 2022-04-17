package text.analysis;

import static java.util.Collections.emptyList;
import java.util.Optional;
import java.util.stream.Collectors;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.Entity;
import text.analysis.person.Person;
import text.analysis.person.PersonExtractionResult;

public class TextRazorExtractor {
  private TextRazor textRazor;

  public TextRazorExtractor(TextRazor textRazor) {
    this.textRazor = textRazor;
  }

  public PersonExtractionResult extractPersons(String text) {
    try {
      return doExtractPersons(text);
    } catch (NetworkException e) {
      throw new RuntimeException("network error", e);
    } catch (AnalysisException e) {
      throw new RuntimeException("text analysis error", e);
    }
  }

  private PersonExtractionResult doExtractPersons(String text)
      throws NetworkException, AnalysisException {
    var response = textRazor.analyze(text).getResponse();
    var persons = Optional.ofNullable(response.getEntities()).orElse(emptyList()) //
        .stream() //
        .filter(entity -> isPerson(entity)) //
        .map(entity -> new Person(entity.getEntityId(), entity.getWikidataId())) //
        .collect(Collectors.toSet());
    var languageCode = LocaleUtils.languageCodeFromIso3(response.getLanguage());

    return new PersonExtractionResult(languageCode, persons);
  }

  private static boolean isPerson(Entity entity) {
    return entity.getType().contains("Person");
  }

}
