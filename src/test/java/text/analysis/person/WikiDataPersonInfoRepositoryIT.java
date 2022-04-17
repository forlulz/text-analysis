package text.analysis.person;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WikiDataPersonInfoRepositoryIT {

  WikiDataPersonInfoRepository sut = new WikiDataPersonInfoRepository();

  @DisplayName("Given russian locale and person, when find, returns infos in russian")
  @Test
  void returnsPersonInfo_russian() {
    var russian = new Locale.Builder().setLanguage("ru").build();

    var infos = sut.find(russian, Set.of(new Person("Chuck Norris", "Q2673")));

    assertThat(infos.iterator().hasNext(), is(true));

    var info = infos.iterator().next();
    assertThat(info.label(), is("Чак Норрис"));
    assertThat(info.description(), notNullValue());
    assertThat(info.imageUrl(), notNullValue());
  }

  @DisplayName("Given locale without language and person, when find, uses default locale")
  @Test
  void returnsPersonInfo_default() {
    sut.setDefaultLocale(Locale.ENGLISH);
    var empty = new Locale.Builder().build();
    assertThat(empty.getLanguage(), emptyString());

    var infos = sut.find(empty, Set.of(new Person("Chuck Norris", "Q2673")));

    assertThat(infos.iterator().hasNext(), is(true));

    var info = infos.iterator().next();
    assertThat(info.label(), is("Chuck Norris"));
    assertThat(info.description(), notNullValue());
    assertThat(info.imageUrl(), notNullValue());
  }

}
