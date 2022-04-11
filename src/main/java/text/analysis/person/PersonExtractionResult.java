package text.analysis.person;

import java.util.Set;

public record PersonExtractionResult(String language, Set<Person> persons) {
}
