package text.analysis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.textrazor.TextRazor;
import text.analysis.App;

@Configuration
@ComponentScan(basePackageClasses = App.class)
public class AppConfiguration {

  @Value("#{environment.TEXTRAZOR_API_KEY}")
  private String textrazorApiKey;

  @Bean
  public TextRazor textRazor() {
    var textRazor = new TextRazor(textrazorApiKey);
    textRazor.addExtractor("entities");

    return textRazor;
  }

}
