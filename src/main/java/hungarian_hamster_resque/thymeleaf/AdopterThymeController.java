package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_resque.services.AdopterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adopters")
@RequiredArgsConstructor
public class AdopterThymeController {

    private final AdopterService adopterService;

    @GetMapping("/add_new_adopter")
    public String showForm(Model model) {
        CreateAdopterCommand adopter = new CreateAdopterCommand();
        model.addAttribute("adopter", adopter);


        return "adopters/add_new_adopter_form";
    }

    @PostMapping("/add_new_adopter")
    public String submitForm(@ModelAttribute("adopter") @Valid CreateAdopterCommand adopter, Model model) {
        String fullAddress = adopter.getZip() + " " + adopter.getTown() + ", " + adopter.getStreet() + " " + adopter.getHouseNumber() +
                adopter.getOther();
        model.addAttribute("fullAddress", fullAddress);
        adopterService.createAdopter(adopter);
        return "adopters/add_new_adopter_succeeded";
    }
}
