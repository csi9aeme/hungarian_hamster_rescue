package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.HamsterDtoWithoutAdoptive;
import hungarian_hamster_resque.services.HamsterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hamsters")
@AllArgsConstructor
public class HamsterThymeController {

    private HamsterService hamsterService;

    @GetMapping("/listofallhamsters")
    public ModelAndView findCurrentHamsters() {
        List<HamsterDtoWithoutAdoptive> hamsters = hamsterService.getListOfCurrentHamsters();
        Map<String, Object> model = Map.of();
        for (HamsterDtoWithoutAdoptive h : hamsters) {
            model = Map.of(
                    "hamsters", hamsters
            );
        }
        return new ModelAndView("listofallcurrenthamsters", model);
    }
}
