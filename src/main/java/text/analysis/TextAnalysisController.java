package text.analysis;

import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import text.analysis.person.PersonData;

@RestController
@RequestMapping("/api/analysis")
@Validated
public class TextAnalysisController {

  @Autowired
  private TextAnalyzer textAnalyzer;

  @PostMapping("/persons")
  public ResponseEntity<List<PersonData>> findPersonInformations(@NotBlank String text) {
    return new ResponseEntity<>(textAnalyzer.findPersonInformations(text), HttpStatus.OK);
  }

}
