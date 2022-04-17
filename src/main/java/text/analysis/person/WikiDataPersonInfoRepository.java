package text.analysis.person;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

public class WikiDataPersonInfoRepository {
  private int imageWidth = 50;
  private Locale defaultLocale = Locale.ENGLISH;

  protected WikibaseDataFetcher buildWikidataDataFetcher() {
    return WikibaseDataFetcher.getWikidataDataFetcher();
  }

  public Iterable<PersonInfo> find(Locale locale, Set<Person> persons) {
    var wikidataIds = persons.stream().map(Person::wikidataId).collect(Collectors.toList());
    final var actualLocale = locale.getLanguage().isEmpty() ? defaultLocale : locale;

    var personInfos = getItemDocuments(wikidataIds)
        .entrySet()
        .stream()
        .map(e -> toPersonInfo(actualLocale, e.getValue()))
        .collect(Collectors.toList());

    return personInfos;
  }

  private Map<String, ItemDocument> getItemDocuments(List<String> entityIds) {
    try {
      return doGetItemDocuments(entityIds);
    } catch (MediaWikiApiErrorException e) {
      throw new RuntimeException("mediawiki api error", e);
    } catch (IOException e) {
      throw new RuntimeException("data fetch error", e);
    }
  }

  private Map<String, ItemDocument> doGetItemDocuments(List<String> entityIds)
      throws MediaWikiApiErrorException, IOException {
    return buildWikidataDataFetcher()
        .getEntityDocuments(entityIds)
        .entrySet()
        .stream()
        .filter(e -> e.getValue() instanceof ItemDocument)
        .collect(Collectors.toMap(Map.Entry::getKey, v -> (ItemDocument) v.getValue()));
  }

  private PersonInfo toPersonInfo(Locale locale, ItemDocument itemDocument) {
    var label = itemDocument.findLabel(locale.getLanguage());
    var description = itemDocument.findDescription(locale.getLanguage());
    var image = itemDocument.findStatementStringValue("P18");
    var imageUrl = buildImageUrl(image.getString());

    return new PersonInfo(label, description, imageUrl);
  }

  private String buildImageUrl(String imageName) {
    return String.format("http://commons.wikimedia.org/w/thumb.php?f=%s&w=%d",
        encodeImageName(imageName), imageWidth);
  }

  private String encodeImageName(String imageName) {
    return URLEncoder.encode(imageName.replace(" ", "_"), UTF_8)
        .replace("%3A", ":")
        .replace("%2F", "/");
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public void setDefaultLocale(Locale defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

}
