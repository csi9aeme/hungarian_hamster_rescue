package hungarian_hamster_rescue.thymeleaf;

import hungarian_hamster_rescue.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_rescue.dtos.hamster.HamsterDto;
import hungarian_hamster_rescue.dtos.hamster.HamsterDtoWithoutAdopter;
import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.services.HamsterService;
import hungarian_hamster_rescue.services.HostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hamsters")
@RequiredArgsConstructor
public class HamsterThymeController {

    private final HamsterService hamsterService;
    private final HostService hostService;

    @GetMapping("/current_hamsters")
    public ModelAndView findCurrentHamsters() {
        List<HamsterDtoWithoutAdopter> hamsters = hamsterService.getListOfCurrentHamsters();
        Map<String, Object> model = Map.of();
        for (HamsterDtoWithoutAdopter h : hamsters) {
            model = Map.of(
                    "hamsters", hamsters
            );
        }
        return new ModelAndView("/hamsters/current_hamsters", model);
    }


    @GetMapping("/create_hamster")
    public String showForm(Model model) {
        CreateHamsterCommand hamster = new CreateHamsterCommand();
        model.addAttribute("hamster", hamster);

        List<String> listSpecies = Arrays.stream(HamsterSpecies.values()).toList()
                .stream()
                .map(HamsterSpecies::getNameOfSpecies)
                .toList();
        model.addAttribute("listSpecies", listSpecies);

        List<String> listGenders = Arrays.stream(Gender.values()).toList()
                .stream()
                .map(Gender::getGender)
                .toList();
        model.addAttribute("listGenders", listGenders);

        List<String> listStatus = Arrays.stream(HamsterStatus.values()).toList()
                .stream()
                .map(HamsterStatus::getStatus)
                .toList();
        model.addAttribute("listStatus", listStatus);


        return "/hamsters/create_hamster_form";
    }

    @PostMapping("/create_hamster")
    public String submitForm(@ModelAttribute("hamster") @Valid CreateHamsterCommand hamster, Model model) {
       //System.out.println(hamster);
        String hostName = hostService.findHostById(hamster.getHostId()).getName();
        hamsterService.createHamster(hamster);
        model.addAttribute("hostName", hostName);
        return "/hamsters/create_hamster_succeeded";
    }

    @GetMapping("/hamster_page/{id}")
    public ModelAndView getHamsterPage(@PathVariable("id") long id) {
        HamsterDto hamster = hamsterService.findHamsterById(id);

        Map<String, Object> model = Map.of(
                "hamster", hamster,
                "adopter", checkIfAdopted(hamster),
                "place", hamsterService.findHamsterPlace(hamster.getId()));


        return new ModelAndView("/hamsters/hamster_page", model);

    }

    private String checkIfAdopted(HamsterDto hamster) {
        if (hamster.getAdopter()==null) {
            return "Not adopted yet";
        }
        return hamster.getAdopter().getName();
    }


}
