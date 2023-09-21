package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.ContactsDto;
import hungarian_hamster_resque.dtos.host.*;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.HostMapper;
import hungarian_hamster_resque.models.*;
import hungarian_hamster_resque.repositories.HostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;

    private final HostMapper hostMapper;


    @Transactional
    public HostDtoWithoutHamsters createHost(CreateHostCommand command) {
        Host host = Host.builder()
                .name(command.getName())
                .address(getAddressFromOther(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther()))
                .contacts(getContactsFromOther(command.getPhoneNumber(), command.getEmail(), command.getOtherContactInfo()))
                .capacity(command.getCapacity())
                .hostStatus(findHostStatus(command.getHostStatus()))
                .hamsters(new ArrayList<>())
                .weeklyReports(new ArrayList<>())
                .build();

        hostRepository.save(host);

        return hostMapper.toDtoWithoutHam(host);
    }

    @Transactional
    public HostDtoWithoutHamsters updateHost(long id, UpdateHostCommand command) {
        Host host = findHostEntityById(id);

        Address newAddress = getAddressFromOther(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());
        Contacts newContacts = getContactsFromOther(command.getPhoneNumber(), command.getEmail(), command.getOtherContactInfo());

        host.setName(command.getName());
        host.setAddress(newAddress);
        host.setContacts(newContacts);
        host.setCapacity(command.getCapacity());
        host.setHostStatus(command.getHostStatus());

        hostRepository.save(host);

        return hostMapper.toDtoWithoutHam(host);
    }


    public List<HostDtoWithoutHamsters> getListOfHosts(Optional<String> hostNamePart) {
        if (hostNamePart.isPresent()) {
            List<Host> hosts = findHostEntitiesByName(hostNamePart.get());

            return hostMapper.toDtoWithoutHam(hosts);
        }

        List<Host> hosts = hostRepository.findAll();

        return hostMapper.toDtoWithoutHam(hosts);
    }

    @Transactional
    public HostDtoWithoutHamsters findHostById(long id) {
        Host host = findHostEntityById(id);
        return hostMapper.toDtoWithoutHam(host);
    }

    //ez törölhető, ha elkészült a név alapján keresés
    @Transactional
    public HostDtoWithHamsters getListOfHostsHamsters(long id) {
        Host host = hostRepository.findByIdWithAllHamster(id);
        if (host.getHamsters().isEmpty()) {
            throw new HostHasNotHamsterYetException(id);
        }

        return hostMapper.toDtoWithHam(host);
    }

    @Transactional
    public List<HostDtoWithHamsters> findHostsByName(String name) {
        List<Host> hosts = hostRepository.findByNameWithAllHamster(name);
        if (hosts.isEmpty()) {
            throw new HostWithNamePartNotExistException(name);
        }

        return hostMapper.toDtoWithHam(hosts);
    }

    public List<HostDtoCountedCapacity> getListOfHostOnlyWithFreeCapacity() {
        List<Host> hosts = hostRepository.findOnlyActiveWithAllHamster();

        List<HostDtoCountedCapacity> hostDto = new ArrayList<>();
        for(int i = 0; i < hosts.size(); i++) {
            //address
            Host host = hosts.get(i);
            HostDtoCountedCapacity hostDtoCap = hostMapper.toDtoFreeCapacity(host);
            hostDtoCap.setLocation(host.getAddress().getTown());
            hostDtoCap.setContactsDto(new ContactsDto(host.getContacts().getPhoneNumber(), host.getContacts().getEmail(), host.getContacts().getOtherContact()));
            hostDtoCap.setFreeCapacity(countFreeCapacityOfAHost(host.getId()));
            hostDtoCap.setCapacityAll(host.getCapacity());

            hostDto.add(hostDtoCap);
        }

        return hostDto;
    }

    public List<HostDtoCountedCapacity> getListOfHostAndDisplayFreeCapacity() {
        List<Host> hosts = hostRepository.findOnlyActiveWithAllHamster();

        List<HostDtoCountedCapacity> hostDto = hostMapper.toDtoFreeCapacity(hosts);
        setFreeCapacity(hosts, hostDto);

        return hostDto;
    }


    public List<HostDtoCountedCapacity> getListOfHostWithFreeCapacityByCity(String city) {
        List<Host> hosts = hostRepository.findOnlyActiveWithAllHamsterByCity(city);
        List<HostDtoCountedCapacity> hostDto = hostMapper.toDtoFreeCapacity(hosts);
        setFreeCapacity(hosts, hostDto);

        return hostDto;
    }

    @Transactional
    public HostDtoWithoutHamsters setHostInactive(long id) {

        Host host = hostRepository.findById(id).orElseThrow(() -> new HostWithIdNotExistException(id));

        clearAllHamstersFromTheList(host);
        host.setHostStatus(HostStatus.INACTIVE);
        return hostMapper.toDtoWithoutHam(host);
    }


    @Transactional
    public List<HostDtoWithHamsters> getListOfHostsWithHamstersByCity(String city) {
        List<Host> result = hostRepository.findByCityWithHamster(city);

        if (result.isEmpty()) {
            throw new HostWithCityNotFoundException(city);
        }

        return hostMapper.toDtoWithHam(result);
    }

    private Host findHostEntityById(long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new HostWithIdNotExistException(id));
    }

    private List<Host> findHostEntitiesByName(String namePart) {
        List<Host> result = hostRepository.findAHostByNameContains(namePart);

        if (result.isEmpty()) {
            throw new HostWithNamePartNotExistException(namePart);
        }
        return result;
    }

    private void clearAllHamstersFromTheList(Host host) {
        if (host.getHamsters().size() > 0) {
            List<Hamster> delete = host.getHamsters();
            delete.forEach(h -> h.setHost(null));

            host.getHamsters().removeAll(delete);
        }
    }

    private HostStatus findHostStatus(String hostStatus) {
        for (HostStatus s : HostStatus.values()) {
            if (s.getHostStatus().equals(hostStatus)) {
                return s;
            }
        }
        throw new HostStatusNotAcceptableException(hostStatus);
    }

    private int countFreeCapacityOfAHost(long id) {
        Host host = hostRepository.findByIdWithAllHamster(id);
        return host.getCapacity() - host.getHamsters().size();
    }

    private void setFreeCapacity(List<Host> hosts, List<HostDtoCountedCapacity> hostDto) {
        for (int i = 0; i < hosts.size(); i++) {
            hostDto.get(i).setFreeCapacity(countFreeCapacityOfAHost(hosts.get(i).getId()));
        }
    }
    private void setAllCapacity(List<Host> hosts, List<HostDtoCountedCapacity> hostDto) {
        for (int i = 0; i < hosts.size(); i++) {
            hostDto.get(i).setCapacityAll(hosts.get(i).getCapacity());
        }
    }

    private Address getAddressFromOther(String... address) {
        return new Address(address[0], address[1], address[2], address[3], address[4]);
    }

    private Contacts getContactsFromOther(String... contacts) {
        return new Contacts(contacts[0], contacts[1], contacts[2]);
    }




}