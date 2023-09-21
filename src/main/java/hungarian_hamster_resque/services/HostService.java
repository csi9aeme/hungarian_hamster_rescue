package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AddressDto;
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

        return getHostDtoWithoutHamstersWithSetAddressAndContacts(host);
    }

    @Transactional
    public HostDtoWithoutHamsters updateHost(long id, UpdateHostCommand command) {
        Host host = findHostEntityById(id);

        Address newAddress = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());
        Contacts newContacts = new Contacts(command.getPhoneNumber(), command.getEmail(), command.getOtherContactInfo());

        host.setName(command.getName());
        host.setAddress(newAddress);
        host.setContacts(newContacts);
        host.setCapacity(command.getCapacity());
        host.setHostStatus(command.getHostStatus());

        hostRepository.save(host);

        return getHostDtoWithoutHamstersWithSetAddressAndContacts(host);
    }


    public List<HostDtoWithoutHamsters> getListOfHosts(Optional<String> hostNamePart) {
        if (hostNamePart.isPresent()) {
            List<Host> hosts = findHostEntitiesByName(hostNamePart.get());

            List<HostDtoWithoutHamsters> result = new ArrayList<>();
            for (Host h : hosts) {
                result.add(getHostDtoWithoutHamstersWithSetAddressAndContacts(h));
            }

            return result;
        }

        List<Host> result = hostRepository.findAll();
        List<HostDtoWithoutHamsters> hostDto = hostMapper.toDtoWithoutHam(result);
        return hostDto;
    }

    @Transactional
    public HostDtoWithoutHamsters findHostById(long id) {

        return hostMapper.toDtoWithoutHam(findHostEntityById(id));
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
        if (hosts.size() == 0) {
            throw new HostWithNamePartNotExistException(name);
        }

        return hostMapper.toDtoWithHam(hosts);
    }

    public List<HostDtoCountedCapacity> getListOfHostWithCapacity() {
        List<Host> hosts = hostRepository.findOnlyActiveWithAllHamster();
        List<HostDtoCountedCapacity> hostDto = hostMapper.toDtoFreeCapacity(hosts);
        setFreeCapacity(hosts, hostDto);

        return hostDto;
    }

    public List<HostDtoCountedCapacity> getListOfHostWithFreeCapacity() {
        List<Host> hosts = hostRepository.getListOfHostWithFreeCapacity();

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

    public List<HostDtoCountedCapacity> getListOfHostWithFreeCapacityByCity(String city) {
        List<Host> hosts = hostRepository.findOnlyActiveWithAllHamsterByCity(city);
        List<HostDtoCountedCapacity> hostDto = hostMapper.toDtoFreeCapacity(hosts);
        setFreeCapacity(hosts, hostDto);

        return hostDto;
    }

    @Transactional
    public HostDtoWithoutHamsters setHostInactive(long id) {

        if (hostRepository.findById(id).isEmpty()) {
            throw new HostWithIdNotExistException(id);
        }

        Host host = hostRepository.findById(id).get();
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
        int freeCapacity = host.getCapacity() - host.getHamsters().size();
        return freeCapacity;
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

    private void setContactsDto(HostDtoWithoutHamsters hostDto, Host host) {
        hostDto.setContactsDto(new ContactsDto(host.getContacts().getPhoneNumber(), host.getContacts().getEmail(), host.getContacts().getOtherContact()));
    }

    //próba
    private ContactsDto setContactsDtoTRY(Host host) {
        return new ContactsDto(host.getContacts().getPhoneNumber(), host.getContacts().getEmail(), host.getContacts().getOtherContact());
    }
    private AddressDto setAddressDtoTRY( Host host) {
       return new AddressDto(host.getAddress().getZip(), host.getAddress().getTown(), host.getAddress().getStreet(), host.getAddress().getHouseNumber(), host.getAddress().getOther());
    }



    //////

    private void setAddressDto(HostDtoWithoutHamsters hostDto, Host host) {
        hostDto.setAddressDto(new AddressDto(host.getAddress().getZip(), host.getAddress().getTown(), host.getAddress().getStreet(), host.getAddress().getHouseNumber(), host.getAddress().getOther()));
    }

    private HostDtoWithoutHamsters getHostDtoWithoutHamstersWithSetAddressAndContacts(Host host) {
        HostDtoWithoutHamsters hostDto = hostMapper.toDtoWithoutHam(host);
        setAddressDto(hostDto, host);
        setContactsDto(hostDto, host);
        return hostDto;
    }
}