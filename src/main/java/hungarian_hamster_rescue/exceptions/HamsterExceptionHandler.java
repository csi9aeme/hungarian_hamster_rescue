package hungarian_hamster_rescue.exceptions;

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
        detail.setType(URI.create("hamsterresque/hamster-with-id-not-found"));

        return detail;
    }

    @ExceptionHandler(HamsterWithNameNotExist.class)
    public ProblemDetail handleHamsterWithNameNotExistException(HamsterWithNameNotExist e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamsters-with-name-not-found"));

        return detail;
    }

    @ExceptionHandler(HamsterSpeciesNotExistException.class)
    public ProblemDetail handleHamsterSpeciesNotExistException(HamsterSpeciesNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamsterspecies-not-exist"));

        return detail;
    }

    @ExceptionHandler(HamsterGenderNotAcceptableException.class)
    public ProblemDetail handleGenderNotAcceptableException(HamsterGenderNotAcceptableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/gender-not-acceptable"));

        return detail;
    }

    @ExceptionHandler(HamsterStatusNotAcceptableException.class)
    public ProblemDetail handleHamsterStatusNotAcceptableException(HamsterStatusNotAcceptableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/status-not-acceptable"));

        return detail;
    }

    @ExceptionHandler(HamsterNotAdoptedYetException.class)
    public ProblemDetail handleHamsterNotAdoptedYetException(HamsterNotAdoptedYetException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/not-adopted-yet"));

        return detail;
    }

}
