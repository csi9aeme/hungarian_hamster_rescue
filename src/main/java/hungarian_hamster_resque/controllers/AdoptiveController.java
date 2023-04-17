package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.AdoptiveDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdoptiveDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.CreateAdoptiveCommand;
import hungarian_hamster_resque.dtos.UpdateAdoptiveCommand;
import hungarian_hamster_resque.services.AdoptiveService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adoptives")
@RequiredArgsConstructor
public class AdoptiveController {

    private final AdoptiveService adoptiveService;

    @GetMapping
    @Operation(summary = "List of adoptives (optional by name)")
    @ResponseStatus(HttpStatus.FOUND)
    public List<AdoptiveDtoWithoutHamsters> getAdoptivesByName(@RequestParam Optional<String> namePart) {
        return adoptiveService.getAdoptives(namePart);
    }
    @GetMapping("/adoptivesbycity")
    @Operation(summary = "List of adoptives by city")
    @ResponseStatus(HttpStatus.FOUND)
    public List<AdoptiveDtoWithoutHamsters> getAdoptivesByCity(@RequestParam String city) {
        return adoptiveService.getAdoptivesByCity(city);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find adoptive by id")
    @ResponseStatus(HttpStatus.FOUND)
    public AdoptiveDtoWithoutHamsters findAdoptiveById(@PathVariable("id") long id) {
        return adoptiveService.findAdoptiveById(id);
    }
    @GetMapping("/{id}/hamsters")
    @Operation(summary = "List of hamsters by host id")
    @ResponseStatus(HttpStatus.FOUND)
    public AdoptiveDtoWithHamsters findAdoptiveByIdWithHamsters(@PathVariable("id") long id) {
        return adoptiveService.findAdoptiveByIdWithHamsters(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new adoptive")
    public AdoptiveDtoWithoutHamsters createAdoptive(@Valid @RequestBody CreateAdoptiveCommand command) {
        return adoptiveService.createAdoptive(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update an existing adoptive")
    public AdoptiveDtoWithoutHamsters updateAdoptive(@PathVariable("id") long id, @Valid @RequestBody UpdateAdoptiveCommand command) {
        return adoptiveService.updateAdoptive(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete adoptive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdoptiveFromDatabase(@PathVariable("id") long id) {
        adoptiveService.deleteAdoptive(id);

    }


}
