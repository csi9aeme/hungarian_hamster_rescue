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
}
