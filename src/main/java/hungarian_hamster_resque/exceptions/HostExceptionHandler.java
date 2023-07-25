package hungarian_hamster_resque.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class HostExceptionHandler {

    @ExceptionHandler(HostWithNamePartNotExistException.class)
    public ProblemDetail handleHostWithNameNotExistException(HostWithNamePartNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/host-not-found"));

        return detail;
    }

    @ExceptionHandler(HostWithCityNotFoundException.class)
    public ProblemDetail handleHostWithCityNotExistException(HostWithCityNotFoundException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/host-not-found"));

        return detail;
    }

    @ExceptionHandler(HostWithIdNotExistException.class)
    public ProblemDetail handleHostWithIdNotExistException(HostWithIdNotExistException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/host-not-found"));

        return detail;
    }

    @ExceptionHandler(HostHasNotHamsterYetException.class)
    public ProblemDetail handleHostHasNotHamsterYetException(HostHasNotHamsterYetException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("hamsterresque/hamsters-not-found"));
        return detail;
    }

    @ExceptionHandler(HostCantTakeMoreHamstersException.class)
    public ProblemDetail handleHostCantTakeMoreHamstersException(HostCantTakeMoreHamstersException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/not-enough-capacity"));

        return detail;
    }

    @ExceptionHandler(HostIsInactiveException.class)
    public ProblemDetail handleHostIsInactiveException(HostIsInactiveException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/inactive-host"));

        return detail;
    }

    @ExceptionHandler(HostStatusNotAcceptableException.class)
    public ProblemDetail handleHostStatusNotAcceptableException(HostStatusNotAcceptableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("hamsterresque/status-not-acceptable"));

        return detail;
    }
}
