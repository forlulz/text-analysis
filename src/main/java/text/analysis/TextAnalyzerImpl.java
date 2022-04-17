package text.analysis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import text.analysis.person.PersonData;
import text.analysis.person.PersonExtractor;
import text.analysis.person.PersonInfo;
import text.analysis.person.PersonInfoRepository;

@Service
public class TextAnalyzerImpl implements TextAnalyzer {

  private @Autowired PersonExtractor personExtractor;
  private @Autowired PersonInfoRepository personInfoRepository;

  @Override
  public List<PersonData> findPersonInformations(String text) {
    var extractionResult = personExtractor.extract(text);
    var language = extractionResult.language();
    var persons = extractionResult.persons();
    var personInfos = personInfoRepository.find(language, persons);

    var personDatas = new ArrayList<PersonData>();
    personInfos.forEach(info -> personDatas.add(toPersonData(info)));

    return personDatas;
  }

  private PersonData toPersonData(PersonInfo info) {
    return new PersonData(info.label(), info.description(), info.imageUrl());
  }

}
