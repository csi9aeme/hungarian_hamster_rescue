package hungarian_hamster_resque.thymeleaf;

import hungarian_hamster_resque.dtos.host.CreateHostCommand;
import hungarian_hamster_resque.dtos.host.HostDtoCountedCapacity;
import hungarian_hamster_resque.dtos.host.HostDtoWithHamsters;
import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.services.HostService;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public String submitForm(@ModelAttribute("host") CreateHostCommand host, Model model) {
        String fullAddress = host.getZip() + " " + host.getTown() + ", " + host.getStreet() + " " + host.getHouseNumber() +
                host.getOther();

        model.addAttribute("fullAddress", fullAddress);
        hostService.createHost(host);
        return "hosts/add_new_host_succeeded";
    }

    @GetMapping("/hosts_all_time")
    @Description("List of all hosts all time")
    public ModelAndView findHostsAllTime(@RequestParam Optional<String> namePart) {
        List<HostDtoWithoutHamsters> hosts = hostService.getListOfHosts(namePart);
        Map<String, Object> model = Map.of(
                "hosts", hosts);
        return new ModelAndView("hosts/hosts_all_time", model);
    }

    @GetMapping("/current_hosts")
    @Description("List all current hosts who is active")
    public ModelAndView findCurrentHosts() {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostAndDisplayFreeCapacity();
        Map<String, Object> model = Map.of(
                "hosts", hosts);
        return new ModelAndView("hosts/current_hosts", model);
    }

    @GetMapping("/current_hosts_free_capacity")
    @Description("List current hosts with free capacity")
    public ModelAndView findCurrentHostsWithFreeCapacity() {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostOnlyWithFreeCapacity();
        Map<String, Object> model = Map.of("hosts", hosts);

        return new ModelAndView("hosts/current_hosts_free_capacity", model);

    }

    @GetMapping("/current_hosts_by_city/{city}")
    public ModelAndView findCurrentHostsByCity(@PathVariable("city") String city) {
        List<HostDtoCountedCapacity> hosts = hostService.getListOfHostWithFreeCapacityByCity(city);
        Map<String, Object> model = Map.of("hosts", hosts, "city", city);


        return new ModelAndView("hosts/current_hosts_by_city", model);

    }


    @GetMapping("/hosts_by_name_and_hamsters/{name}")
    public ModelAndView findHostsByName(@PathVariable("name") String name) {
        List<HostDtoWithHamsters> hosts = hostService.findHostsByName(name);

        Map<String, Object> model = Map.of("hosts", hosts);

        return new ModelAndView("hosts/hosts_by_name_and_hamsters", model);
    }
    @GetMapping("/host_current_hamsters/{name}")
    public ModelAndView listHamstersOfAHost(@PathVariable("name") String name) {
        HostDtoWithHamsters host = hostService.findHostByNameWithAllHamsters(name);
        Map<String, Object> model = Map.of(
                "host", host,
                "hamsters", host.getHamsters(),
                "fullAddress", host.getAddressDto().getZip() + " " + host.getAddressDto().getTown() + ", " +
                        host.getAddressDto().getStreet() + " " + host.getAddressDto().getHouseNumber() +
                        host.getAddressDto().getOther()

        );
        return new ModelAndView("hosts/host_current_hamsters", model);
    }


}