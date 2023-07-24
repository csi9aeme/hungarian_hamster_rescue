package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.services.HamsterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hamsters")
@AllArgsConstructor
public class HamsterController {

    private HamsterService hamsterService;

    @GetMapping
    @Operation(summary = "List of all hamster by name.")
    public List<HamsterDto> getListOfHamsters(@RequestParam Optional<String> namePart) {
        return hamsterService.getListOfHamsters(namePart);
    }

    //thymeleaf done
    @GetMapping("/fostering")
    @Operation(summary = "List of current fostering hamsters")
    public List<HamsterDtoWithoutAdoptive> getListOfCurrentHamsters() {
        return hamsterService.getListOfCurrentHamsters();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    @Operation(summary = "Find a adoptable hamster by ID.")
    public HamsterDtoWithoutAdoptive findAdoptableHamsterById(@PathVariable("id") long id) {
        return hamsterService.findAdoptableHamsterById(id);
    }

    //thymeleaf in progress
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new hamster.")
    public HamsterDtoWithoutAdoptive createHamster(@Valid @RequestBody CreateHamsterCommand command) {
        return hamsterService.createHamster(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update an existed hamster's attributes.")
    public HamsterDtoWithoutAdoptive updateHamsterAllAttributes(@PathVariable("id") long id, @Valid @RequestBody UpdateHamsterCommand command) {
        return hamsterService.updateHamsterAllAttributes(id, command);
    }

    @PutMapping("/{id}/adopted")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Set adoption status an existed hamster")
    public HamsterDto adoptHamster(@PathVariable("id") long id, @Valid @RequestBody AdoptHamsterCommand command) {
        return hamsterService.adoptHamster(id, command);
    }



}
