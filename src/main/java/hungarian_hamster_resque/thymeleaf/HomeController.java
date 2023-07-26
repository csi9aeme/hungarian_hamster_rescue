package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.HostDtoWithHamsters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public ModelAndView getHomePage() {

        return new ModelAndView("index");
    }
    @GetMapping("/hamster_menu")
    public ModelAndView getHamsterMenu() {

        return new ModelAndView("hamster_menu");
    }

    @GetMapping("/host_menu")
    public ModelAndView getHostMenu() {

        return new ModelAndView("host_menu");
    }

    @GetMapping("/adoptive_menu")
    public ModelAndView getAdoptiveMenu() {

        return new ModelAndView("adoptive_menu");
    }
}
