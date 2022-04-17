package text.analysis.person;

import static java.util.Collections.emptyList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.Entity;
import text.analysis.util.LocaleUtils;

@Component
public class TextRazorPersonExtractor implements PersonExtractor {
  private TextRazor textRazor;

  public TextRazorPersonExtractor(TextRazor textRazor) {
    this.textRazor = textRazor;
  }

  @Override
  public PersonExtractionResult extract(String text) {
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
    var types = entity.getType();
    return types != null && types.contains("Person");
  }

}
