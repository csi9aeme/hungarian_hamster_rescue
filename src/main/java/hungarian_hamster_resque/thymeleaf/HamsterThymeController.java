package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.HamsterDtoWithoutAdoptive;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.services.HamsterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hamsters")
@AllArgsConstructor
public class HamsterThymeController {

    private HamsterService hamsterService;

    @GetMapping("/currentlyallhamsters")
    public ModelAndView findCurrentHamsters() {
        List<HamsterDtoWithoutAdoptive> hamsters = hamsterService.getListOfCurrentHamsters();
        Map<String, Object> model = Map.of();
        for (HamsterDtoWithoutAdoptive h : hamsters) {
            model = Map.of(
                    "hamsters", hamsters
            );
        }
        return new ModelAndView("hamster/currentlyallhamsters", model);
    }

//    @PostMapping("addnewhamster")
//    public ModelAndView createHamster(@Valid @RequestBody CreateHamsterCommand command){
//
//        return new ModelAndView("hamster/addnewhamster",model);
//
//    }


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
    public String submitForm(@ModelAttribute("hamster") CreateHamsterCommand hamster) {
        System.out.println(hamster);
        return "/hamsters/create_hamster_succeeded";
    }
}
