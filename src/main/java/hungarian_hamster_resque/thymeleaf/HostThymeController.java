package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.CreateHostCommand;
import hungarian_hamster_resque.dtos.HostDtoWithHamsters;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.services.HostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hosts")
@AllArgsConstructor
public class HostThymeController {

    private HostService hostService;


    @GetMapping("/add_new_host")
    public String showForm(Model model) {
        CreateHostCommand host = new CreateHostCommand();
        model.addAttribute("host", host);

        List<String> listStatus = Arrays.stream(HostStatus.values()).toList()
                .stream()
                .map(HostStatus::getHostStatus)
                .toList();
        model.addAttribute("listStatus", listStatus);

        return "hosts/add_new_host_form";
    }

    @PostMapping("/add_new_host")
    public String submitForm(@ModelAttribute("host") CreateHostCommand host) {
        System.out.println(host);
        hostService.createHost(host);
        return "/hosts/add_new_host_succeeded";
    }


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
