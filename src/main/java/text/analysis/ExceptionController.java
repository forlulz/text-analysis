package text.analysis;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController {

  public record ValidationError(String field, String message) {
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ConstraintViolationException.class)
  public List<ValidationError> getValidationErrors(ConstraintViolationException ex) {
    return ex.getConstraintViolations().stream().map(this::toValidationError).collect(toList());
  }

  private ValidationError toValidationError(ConstraintViolation<?> constraintViolation) {
    var field = stream(constraintViolation.getPropertyPath().spliterator(), false) //
        .reduce((a, b) -> b) //
        .orElse(null) //
        .toString();
    return new ValidationError(field, constraintViolation.getMessage());
  }

}
