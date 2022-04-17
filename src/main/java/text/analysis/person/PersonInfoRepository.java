package text.analysis.person;

import java.util.Set;

public interface PersonInfoRepository {

  Iterable<PersonInfo> find(String language, Set<Person> persons);

}
