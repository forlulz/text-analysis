package text.analysis.person;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

@ExtendWith(MockitoExtension.class)
class WikiDataPersonInfoRepositoryTest {

  @Spy
  WikiDataPersonInfoRepository sut = new WikiDataPersonInfoRepository();

  @Mock
  WikibaseDataFetcher mockDataFetcher;

  @Test
  void throwsRuntimeExceptionOnMediaWikiApiErrorException() throws Exception {
    when(mockDataFetcher.getEntityDocuments(anyList())).thenThrow(MediaWikiApiErrorException.class);
    doReturn(mockDataFetcher).when(sut).buildWikidataDataFetcher();

    var persons = Set.of(new Person("Chuck Norris", "Q2673"));

    var ex = assertThrows(RuntimeException.class, () -> sut.find(null, persons));
    assertThat(ex.getMessage(), is("mediawiki api error"));
  }

  @Test
  void throwsRuntimeExceptionOnIoException() throws Exception {
    when(mockDataFetcher.getEntityDocuments(anyList())).thenThrow(IOException.class);
    doReturn(mockDataFetcher).when(sut).buildWikidataDataFetcher();

    var persons = Set.of(new Person("Chuck Norris", "Q2673"));

    var ex = assertThrows(RuntimeException.class, () -> sut.find(null, persons));
    assertThat(ex.getMessage(), is("data fetch error"));
  }

}
