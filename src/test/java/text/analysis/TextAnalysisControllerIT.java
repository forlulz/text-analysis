package text.analysis;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import text.analysis.configuration.AppConfiguration;

@WebMvcTest(controllers = TextAnalysisController.class)
@ContextConfiguration(classes = AppConfiguration.class)
@ExtendWith(SpringExtension.class)
public class TextAnalysisControllerIT {

  @Autowired
  MockMvc mockMvc;

  @Value("classpath:article.txt")
  Resource article;

  @Test
  void findsInfosAboutPersonsMentionedInGivenText() throws Exception {
    var text = Files.readString(article.getFile().toPath(), StandardCharsets.UTF_8);

    mockMvc.perform(post("/api/analysis/persons").param("text", text)) //
        .andExpect(status().isOk()) //
        .andExpect(jsonPath("$").isArray()) //
        .andExpect(jsonPath("$", hasSize(greaterThan(1)))) //
        .andExpect(jsonPath("$..label").exists()) //
        .andExpect(jsonPath("$..description").exists()) //
        .andExpect(jsonPath("$..imageUrl").exists());
  }

  @Test
  void givenNoText_returns4xxError() throws Exception {
    mockMvc.perform(post("/api/analysis/persons").param("text", "")) //
        .andExpect(status().is4xxClientError()) //
        .andExpect(jsonPath("$", hasSize(1))) //
        .andExpect(jsonPath("$[0].field", is("text"))) //
        .andExpect(jsonPath("$[0].message", is("must not be blank")));
  }

}
