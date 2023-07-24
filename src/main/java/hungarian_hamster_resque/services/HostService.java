package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.HostHasNotHamsterYetException;
import hungarian_hamster_resque.exceptions.HostWithCityNotFoundException;
import hungarian_hamster_resque.exceptions.HostWithIdNotExistException;
import hungarian_hamster_resque.exceptions.HostWithNamePartNotExistException;
import hungarian_hamster_resque.mappers.HostMapper;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import hungarian_hamster_resque.repositories.HamsterRepository;
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
                .address(command.getAddress())
                .capacity(command.getCapacity())
                .hostStatus(command.getHostStatus())
                .hamsters(new ArrayList<>())
                .build();

        hostRepository.save(host);
        return hostMapper.toDtoWithoutHam(host);
    }

    @Transactional
    public HostDtoWithoutHamsters updateHost(long id, UpdateHostCommand command) {
        Host host = findHostEntityById(id);
        host.setName(command.getName());
        host.setAddress(command.getAddress());
        host.setCapacity(command.getCapacity());
        host.setHostStatus(command.getHostStatus());

        hostRepository.save(host);
        return hostMapper.toDtoWithoutHam(host);
    }

    @Transactional
    public List<HostDtoWithoutHamsters> getListOfHosts(Optional<String> hostNamePart) {
        if (hostNamePart.isPresent()) {

            List<Host> result = findHostEntityByName(hostNamePart);

            return hostMapper.toDtoWithoutHam(result);
        }

        return hostMapper.toDtoWithoutHam(hostRepository.findAll());
    }


    @Transactional
    public HostDtoWithoutHamsters findHostById(long id) {
        return hostMapper.toDtoWithoutHam(findHostEntityById(id));
    }

    @Transactional
    public HostDtoWithHamsters getListOfHostsHamsters(long id) {
        Host host = hostRepository.findByIdWithAllHamster(id);
        if (host.getHamsters().isEmpty()) {
            throw new HostHasNotHamsterYetException(id);
        }
        return hostMapper.toDtoWithHam(host);
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

}