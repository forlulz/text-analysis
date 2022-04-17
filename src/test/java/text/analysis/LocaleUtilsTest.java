package text.analysis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocaleUtilsTest {

  @DisplayName("Given ISO 639-2 three-letter language code, returns matching two-letter code")
  @Test
  void returnsLanguageTwoLetterCode() {
    assertThat(LocaleUtils.languageCodeFromIso3("eng"), is("en"));
    assertThat(LocaleUtils.languageCodeFromIso3("fra"), is("fr"));
    assertThat(LocaleUtils.languageCodeFromIso3("rus"), is("ru"));
  }

  @DisplayName("Given unknown language code, returns null")
  @Test
  void returnsNullForUnknownCodes() {
    assertThat(LocaleUtils.languageCodeFromIso3("wrong"), nullValue());
  }

}
