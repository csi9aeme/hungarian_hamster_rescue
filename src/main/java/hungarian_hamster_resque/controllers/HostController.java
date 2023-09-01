package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.host.CreateHostCommand;
import hungarian_hamster_resque.dtos.host.HostDtoWithHamsters;
import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.host.UpdateHostCommand;
import hungarian_hamster_resque.services.HostService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hosts")
@AllArgsConstructor
public class HostController {

    private HostService hostService;


    @GetMapping
    @Operation(summary = "List of all hosts by name.")
    public List<HostDtoWithoutHamsters> getListOfHosts(@RequestParam Optional<String> namePart) {
        return hostService.getListOfHosts(namePart);
    }


    @GetMapping("/bycity")
    @Operation(summary = "Find hosts by city")
    public List<HostDtoWithHamsters> getListOfHostsWithHamstersByCity(@RequestParam String city) {
        return hostService.getListOfHostsWithHamstersByCity(city);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Find a host by ID.")
    public HostDtoWithoutHamsters findHostById(@PathVariable("id") long id) {
        return hostService.findHostById(id);
    }


    @GetMapping("/{id}/hamsters")
    @Operation(summary = "List of the host's current hamsters.")
    public HostDtoWithHamsters getListOfHostsHamsters(@PathVariable("id") long id) {
        return hostService.getListOfHostsHamsters(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new host to the database.")
    public HostDtoWithoutHamsters createHost(@Valid @RequestBody CreateHostCommand command) {
        return hostService.createHost(command);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update an existed host's attributes.")
    public HostDtoWithoutHamsters updateHost(@PathVariable("id") long id, @Valid @RequestBody UpdateHostCommand command) {
        return hostService.updateHost(id, command);
    }


    @PutMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Set a host's status to inactive.")
    public HostDtoWithoutHamsters setHostInactive(@PathVariable("id") long id) {
        return hostService.setHostInactive(id);
    }

}
