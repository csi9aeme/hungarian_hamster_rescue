package hungarian_hamster_resque.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class AdoptiveExceptionHandler {

    @ExceptionHandler(AdoptiveWithNameNotExistException.class)
    public ProblemDetail handleAdoptiveWithNameNotExistException(AdoptiveWithNameNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adoptive-not-found"));

        return detail;
    }

    @ExceptionHandler(AdoptiveWithIdNotExistException.class)
    public ProblemDetail handleAdoptiveWithIdNotExistException(AdoptiveWithIdNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adoptive-not-found"));

        return detail;
    }
    @ExceptionHandler(AdoptiveWithCityNotExistException.class)
    public ProblemDetail handleAdoptiveWithCityNotExistException(AdoptiveWithCityNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adoptive-not-found"));

        return detail;
    }

    @ExceptionHandler(AdoptiveCantDeleteBecauseHamstersListNotEmptyException.class)
    public ProblemDetail handleAdoptiveCantDeleteException(AdoptiveCantDeleteBecauseHamstersListNotEmptyException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/adoptive-cant-delete"));

        return detail;
    }
}
