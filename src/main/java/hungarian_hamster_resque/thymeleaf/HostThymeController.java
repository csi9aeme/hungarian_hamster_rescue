package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.*;
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


    @GetMapping("/host_current_hamsters/{id}")
    public ModelAndView listOfHostOfHamsters(@PathVariable("id") long hostId) {
        HostDtoWithHamsters host = hostService.getListOfHostsHamsters(hostId);
        Map<String, Object> model = Map.of(
                "host", host.getName(),
                "hamsters", host.getHamsters()
        );
        return new ModelAndView("/hosts/host_current_hamsters", model);
    }

    @GetMapping("/current_hosts")
    public ModelAndView findCurrentHosts() {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostWithCapacity();
        Map<String, Object> model = Map.of();

        for (HostDtoCountedCapacity h : hosts) {
            model = Map.of(
                    "hosts", hosts
            );
        }
        return new ModelAndView("/hosts/current_hosts", model);

    }


    @GetMapping("/current_hosts_free_capacity")
    public ModelAndView findCurrentHostsWithFreeCapacity() {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostWithFreeCapacity();
        Map<String, Object> model = Map.of();
        for (HostDtoCountedCapacity h : hosts) {
            model = Map.of(
                    "hosts", hosts
            );
        }
        return new ModelAndView("/hosts/current_hosts_free_capacity", model);

    }

    @GetMapping("/current_hosts_by_city/{city}")
    public ModelAndView findCurrentHostsByCity(@PathVariable("city") String city) {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostWithFreeCapacityByCity(city);
        Map<String, Object> model = Map.of();
        for (HostDtoCountedCapacity h : hosts) {
            model = Map.of(
                    "hosts", hosts
            );
        }
        return new ModelAndView("/hosts/current_hosts_by_city", model);

    }


    @GetMapping("/hosts_by_name_and_hamsters/{name}")
    public ModelAndView findHostsByName(@PathVariable("name") String name) {
        List<HostDtoWithHamsters> hosts = hostService.findHostsByName(name);

        Map<String, Object> model = Map.of();
        for (HostDtoWithHamsters h : hosts) {
            model = Map.of(
                    "hosts", hosts
            );
        }

        return new ModelAndView("hosts/hosts_by_name_and_hamsters", model);
    }

    @GetMapping("/hosts_by_name_and_hamsters/{name}")
    public ModelAndView findHostsByNameAndHamsters(@PathVariable("name") String name) {
        List<HostDtoWithHamsters> hosts = hostService.findHostsByName(name);

        Map<String, Object> model = Map.of();
        for (HostDtoWithHamsters h : hosts) {
            model = Map.of(
                    "hosts", hosts
            );
        }

        return new ModelAndView("/hosts/hosts_by_name_and_hamsters", model);
    }
}