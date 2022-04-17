package text.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleUtils {
  private static final Map<String, String> LANGUAGES;

  static {
    var isoLanguages = Locale.getISOLanguages();
    var languages = new HashMap<String, String>(isoLanguages.length);

    for (String language : isoLanguages) {
      var locale = new Locale(language);
      languages.put(locale.getISO3Language(), locale.getLanguage());
    }

    LANGUAGES = Collections.unmodifiableMap(languages);
  }

  /**
   * Returns the two-letter language code (ISO 639) corresponding to the given three-letter
   * abbreviation of the given language (ISO 639-2).
   *
   * @param iso3 three-letter language abbreviation (ISO 639-2)
   * @return the correspinding two-letter language code, or {@code null} if no match found
   */
  public static String languageCodeFromIso3(String iso3) {
    return LANGUAGES.get(iso3);
  }

}
