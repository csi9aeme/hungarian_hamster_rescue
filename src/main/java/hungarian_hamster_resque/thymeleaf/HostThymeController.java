package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.HostDtoWithHamsters;
import hungarian_hamster_resque.services.HostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/hosts")
@AllArgsConstructor
public class HostThymeController {

    private HostService hostService;


    @GetMapping("/{id}")
    public ModelAndView findHostById(@PathVariable("id")long hostId) {
        HostDtoWithHamsters host = hostService.getListOfHostsHamsters(hostId);
        Map<String, Object> model = Map.of(
                "host", host.getName(),
                "hamsters", host.getHamsters()
        );
        return new ModelAndView("hostandhamsters", model);
    }


}
