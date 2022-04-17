package text.analysis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;
import com.textrazor.annotations.Response;
import text.analysis.person.Person;

@ExtendWith(MockitoExtension.class)
class TextRazorExtractorTest {
  private static final String PERSON_NAME = "Alexander Afanasyev";
  private static final String PERSON_WIKIDATA_ID = "Q325004";

  TextRazorExtractor sut;

  @Mock
  TextRazor mockTextRazor;

  @Mock
  AnalyzedText mockAnalyzedteText;

  @Mock
  Response mockResponse;

  @Mock
  Entity mockEntity;

  @BeforeEach
  void setUpBeforeEach() throws Exception {
    sut = new TextRazorExtractor(mockTextRazor);
  }

  @DisplayName("Given text, when extract persons, returns detected language two-letter code")
  @Test
  void returnsDetectedLanguage() throws Exception {
    when(mockTextRazor.analyze(any())).thenReturn(mockAnalyzedteText);
    when(mockAnalyzedteText.getResponse()).thenReturn(mockResponse);
    when(mockResponse.getLanguage()).thenReturn("eng");

    var result = sut.extractPersons("any english text");

    assertThat(result.language(), is("en"));
    verify(mockResponse).getLanguage();
  }

  @DisplayName("Given text, when extract persons, returns extracted persons")
  @Test
  void returnsExtractedPersons() throws Exception {
    when(mockTextRazor.analyze(any())).thenReturn(mockAnalyzedteText);
    when(mockAnalyzedteText.getResponse()).thenReturn(mockResponse);
    when(mockEntity.getType()).thenReturn(List.of("Agent", "Person", "Author"));
    when(mockEntity.getEntityId()).thenReturn(PERSON_NAME);
    when(mockEntity.getWikidataId()).thenReturn(PERSON_WIKIDATA_ID);
    when(mockResponse.getEntities()).thenReturn(List.of(mockEntity));
    var expectedPerson = new Person(PERSON_NAME, PERSON_WIKIDATA_ID);

    var result = sut.extractPersons(PERSON_NAME);

    assertThat(result.persons(), contains(expectedPerson));
  }

  @DisplayName("Given network exception, when extract persons, rethrows runtime exception")
  @Test
  void throwsRuntimeExceptionOnNetworkException() throws Exception {
    when(mockTextRazor.analyze(any())).thenThrow(NetworkException.class);

    var ex = assertThrows(RuntimeException.class, () -> sut.extractPersons("text"));
    assertThat(ex.getMessage(), is("network error"));
  }

  @DisplayName("Given analysis exception, when extract persons, rethrows runtime exception")
  @Test
  void throwsRuntimeExceptionOnAnalysisException() throws Exception {
    when(mockTextRazor.analyze(any())).thenThrow(AnalysisException.class);

    var ex = assertThrows(RuntimeException.class, () -> sut.extractPersons("text"));
    assertThat(ex.getMessage(), is("text analysis error"));
  }

}
