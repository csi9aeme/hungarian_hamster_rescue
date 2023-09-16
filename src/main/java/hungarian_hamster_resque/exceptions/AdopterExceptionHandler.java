package hungarian_hamster_resque.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class AdopterExceptionHandler {

    @ExceptionHandler(AdopterWithNameNotExistException.class)
    public ProblemDetail handleAdopterWithNameNotExistException(AdopterWithNameNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adopter-with-name-not-found"));

        return detail;
    }

    @ExceptionHandler(AdopterWithIdNotExistException.class)
    public ProblemDetail handleAdopterWithIdNotExistException(AdopterWithIdNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adopter-with-id-not-found"));

        return detail;
    }
    @ExceptionHandler(AdopterWithCityNotExistException.class)
    public ProblemDetail handleAdopterWithCityNotExistException(AdopterWithCityNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adopter-in-city-not-found"));

        return detail;
    }

    @ExceptionHandler(AdopterCantDeleteBecauseHamstersListNotEmptyException.class)
    public ProblemDetail handleAdopterCantDeleteException(AdopterCantDeleteBecauseHamstersListNotEmptyException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adopter-cannot-delete"));

        return detail;
    }
}
