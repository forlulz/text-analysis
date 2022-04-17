package text.analysis.person;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WikiDataPersonInfoRepositoryIT {

  WikiDataPersonInfoRepository sut = new WikiDataPersonInfoRepository();

  @DisplayName("Given russian language and person, when find, returns infos in russian")
  @Test
  void returnsPersonInfo_russian() {

    var infos = sut.find("ru", Set.of(new Person("Chuck Norris", "Q2673")));

    assertThat(infos.iterator().hasNext(), is(true));

    var info = infos.iterator().next();
    assertThat(info.label(), is("Чак Норрис"));
    assertThat(info.description(), notNullValue());
    assertThat(info.imageUrl(), notNullValue());
  }

  @DisplayName("Given no language and person, when find, uses default locale")
  @Test
  void returnsPersonInfo_default() {
    sut.setDefaultLanguage(Locale.ENGLISH.getLanguage());

    var infos = sut.find(null, Set.of(new Person("Chuck Norris", "Q2673")));

    assertThat(infos.iterator().hasNext(), is(true));

    var info = infos.iterator().next();
    assertThat(info.label(), is("Chuck Norris"));
    assertThat(info.description(), notNullValue());
    assertThat(info.imageUrl(), notNullValue());
  }

}
