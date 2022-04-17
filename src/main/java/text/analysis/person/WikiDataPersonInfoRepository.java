package text.analysis.person;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

@Repository
public class WikiDataPersonInfoRepository implements PersonInfoRepository {
  private int imageWidth = 50;
  private String defaultLanguage = Locale.ENGLISH.getLanguage();

  protected WikibaseDataFetcher buildWikidataDataFetcher() {
    return WikibaseDataFetcher.getWikidataDataFetcher();
  }

  @Override
  public Iterable<PersonInfo> find(String language, Set<Person> persons) {
    var wikidataIds = persons.stream() //
        .map(Person::wikidataId) //
        .filter(StringUtils::isNotBlank) //
        .collect(Collectors.toList());

    var personInfos = getItemDocuments(wikidataIds) //
        .entrySet() //
        .stream() //
        .map(e -> toPersonInfo(defaultIfBlank(language, defaultLanguage), e.getValue())) //
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
    return buildWikidataDataFetcher() //
        .getEntityDocuments(entityIds) //
        .entrySet() //
        .stream() //
        .filter(e -> e.getValue() instanceof ItemDocument) //
        .collect(Collectors.toMap(Map.Entry::getKey, v -> (ItemDocument) v.getValue()));
  }

  private PersonInfo toPersonInfo(String language, ItemDocument itemDocument) {
    var label = itemDocument.findLabel(language);
    var description = itemDocument.findDescription(language);
    var image = itemDocument.findStatementStringValue("P18");
    var imageUrl = image == null ? null : buildImageUrl(image.getString());

    return new PersonInfo(label, description, imageUrl);
  }

  private String buildImageUrl(String imageName) {
    return String.format("http://commons.wikimedia.org/w/thumb.php?f=%s&w=%d",
        encodeImageName(imageName), imageWidth);
  }

  private String encodeImageName(String imageName) {
    return URLEncoder.encode(imageName.replace(" ", "_"), UTF_8) //
        .replace("%3A", ":") //
        .replace("%2F", "/");
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public void setDefaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

}
