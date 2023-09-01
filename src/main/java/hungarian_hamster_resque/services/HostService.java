package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.host.*;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.AddressMapper;
import hungarian_hamster_resque.mappers.HostMapper;
import hungarian_hamster_resque.models.Address;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
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

    private final AddressMapper addressMapper;

    @Transactional
    public HostDtoWithoutHamsters createHost(CreateHostCommand command) {
        Host host = Host.builder()
                .name(command.getName())
                .address(getAddress(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther()))
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
        Address newAddress = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther())
;
        Host host = findHostEntityById(id);
        host.setName(command.getName());
        host.setAddress(getAddress(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther()));
        host.setCapacity(command.getCapacity());
        host.setHostStatus(command.getHostStatus());

        hostRepository.save(host);
        HostDtoWithoutHamsters hostDtoWithoutHamsters = hostMapper.toDtoWithoutHam(host);
        hostDtoWithoutHamsters.setAddressDto(addressMapper.toAddressDto(newAddress));

        return hostDtoWithoutHamsters;
    }

    @Transactional
    public List<HostDtoWithoutHamsters> getListOfHosts(Optional<String> hostNamePart) {
        if (hostNamePart.isPresent()) {
            List<Host> result = findHostEntityByName(hostNamePart);

            return getHostDtoWithoutHamsters(result);
        }

        List<Host> result = hostRepository.findAll();
        return getHostDtoWithoutHamsters(result);
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
            throw  new HostWithNamePartNotExistException(name);
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

    private List<Host> findHostEntityByName(Optional<String> hostNamePart) {
        List<Host> result = hostRepository.findByNameWithoutHamster(hostNamePart.get());

        if (result.isEmpty()) {
            throw new HostWithNamePartNotExistException(hostNamePart.get());
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
        for (int i = 0; i< hosts.size(); i++) {
            hostDto.get(i).setFreeCapacity(countFreeCapacityOfAHost(hosts.get(i).getId()));
        }
    }

    private Address getAddress(String zip, String town, String street, String houseNumber, String other ) {
        return new Address(zip, town, street, houseNumber, other);
    }


    private List<HostDtoWithoutHamsters> getHostDtoWithoutHamsters(List<Host> result) {
        List<HostDtoWithoutHamsters> hostDtoWithoutHamsters = hostMapper.toDtoWithoutHam(result);
        for(int i = 0; i < result.size(); i++) {
            hostDtoWithoutHamsters.get(i).setAddressDto(addressMapper.toAddressDto(result.get(i).getAddress()));
        }
        return hostDtoWithoutHamsters;
    }
}