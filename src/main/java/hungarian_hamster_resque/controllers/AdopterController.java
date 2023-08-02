package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.UpdateAdopterCommand;
import hungarian_hamster_resque.services.AdoptiveService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adopters")
@RequiredArgsConstructor
public class AdopterController {

    private final AdoptiveService adoptiveService;

    @GetMapping
    @Operation(summary = "List of adopters (optional by name)")
    @ResponseStatus(HttpStatus.FOUND)
    public List<AdopterDtoWithoutHamsters> getAdoptersByName(@RequestParam Optional<String> namePart) {
        return adoptiveService.getAdoptives(namePart);
    }
    @GetMapping("/adoptersbycity")
    @Operation(summary = "List of adopters by city")
    @ResponseStatus(HttpStatus.FOUND)
    public List<AdopterDtoWithoutHamsters> getAdoptivesByCity(@RequestParam String city) {
        return adoptiveService.getAdoptivesByCity(city);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find adopter by id")
    @ResponseStatus(HttpStatus.FOUND)
    public AdopterDtoWithoutHamsters findAdoptiveById(@PathVariable("id") long id) {
        return adoptiveService.findAdoptiveById(id);
    }
    @GetMapping("/{id}/hamsters")
    @Operation(summary = "List of hamsters by host id")
    @ResponseStatus(HttpStatus.FOUND)
    public AdopterDtoWithHamsters findAdoptiveByIdWithHamsters(@PathVariable("id") long id) {
        return adoptiveService.findAdoptiveByIdWithHamsters(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new adopter")
    public AdopterDtoWithoutHamsters createAdoptive(@Valid @RequestBody CreateAdopterCommand command) {
        return adoptiveService.createAdoptive(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update an existing adopter")
    public AdopterDtoWithoutHamsters updateAdoptive(@PathVariable("id") long id, @Valid @RequestBody UpdateAdopterCommand command) {
        return adoptiveService.updateAdoptive(id, command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete adopter")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdopterFromDatabase(@PathVariable("id") long id) {
        adoptiveService.deleteAdoptive(id);

    }


}
