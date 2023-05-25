package hungarian_hamster_resque.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class HamsterExceptionHandler {
    @ExceptionHandler(HamsterWithIdNotExistException.class)
    public ProblemDetail handleIllegalArgumentException(HamsterWithIdNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamster-not-found"));

        return detail;
    }

    @ExceptionHandler(HamsterWithNameNotExist.class)
    public ProblemDetail handleHamsterWithNameNotExistException(HamsterWithNameNotExist e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamsters-not-found"));

        return detail;
    }

    @ExceptionHandler(HamsterSpeciesNotExistException.class)
    public ProblemDetail handleHamsterSpeciesNotExistException(HamsterSpeciesNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamsterspecies-not-exist"));

        return detail;
    }

}
