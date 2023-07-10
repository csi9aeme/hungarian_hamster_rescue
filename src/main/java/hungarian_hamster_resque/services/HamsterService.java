package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.HamsterMapper;
import hungarian_hamster_resque.models.*;
import hungarian_hamster_resque.repositories.AdoptiveRepository;
import hungarian_hamster_resque.repositories.HamsterRepository;
import hungarian_hamster_resque.repositories.HostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HamsterService {

    private final HamsterMapper hamsterMapper;
    private final HamsterRepository hamsterRepository;
    private final HostRepository hostRepository;
    private final AdoptiveRepository adoptiveRepository;


    public List<HamsterDto> getListOfHamsters(Optional<String> hamsterName) {
        if (hamsterName.isPresent()) {
            List<Hamster> result = findHamsterWithNamePart(hamsterName.get());
            return hamsterMapper.toDto(result);
        }

        return hamsterMapper.toDto(hamsterRepository.findAll());
    }

    public List<HamsterDtoWithoutAdoptive> getListOfCurrentHamsters() {
        List<Hamster> hamstersWithoutOwner = hamsterRepository.findFosteringHamsters();

        return hamsterMapper.toDtoWithoutAdoptive(hamstersWithoutOwner);

    }

    public HamsterDtoWithoutAdoptive findAdoptableHamsterById(long id) {
        return hamsterMapper.toDtoWithoutAdoptive(findHamsterEntityById(id));
    }

    @Transactional
    public HamsterDtoWithoutAdoptive createHamster(CreateHamsterCommand command) {
        Host host = checkHostIsAvailable(command.getHostId());


        Hamster hamster = Hamster.builder()
                .name(command.getName())
                .hamsterSpecies(findHamsterSpecies(command.getHamsterSpecies()))
                .gender(findGender(command.getGender()))
                .dateOfBirth(command.getDateOfBirth())
                .hamsterStatus(findHamsterStatus(command.getHamsterStatus()))
                .host(host)
                .startOfFostering(command.getStartOfFoster())
                .build();
        host.addHamster(hamster);

        hamsterRepository.save(hamster);

        return hamsterMapper.toDtoWithoutAdoptive(hamster);
    }


    @Transactional
    public HamsterDtoWithoutAdoptive updateHamsterAllAttributes(long id, UpdateHamsterCommand command) {
        Hamster hamsterForUpdate = findHamsterEntityById(id);

        hamsterForUpdate.setName(command.getName());
        hamsterForUpdate.setHamsterSpecies(findHamsterSpecies(command.getHamsterSpecies()));
        hamsterForUpdate.setGender(findGender(command.getGender()));
        hamsterForUpdate.setDateOfBirth(command.getDateOfBirth());
        hamsterForUpdate.setHamsterStatus(findHamsterStatus(command.getHamsterStatus()));

        Host host = hostRepository.findById(command.getHostId())
                .orElseThrow(() -> new HostWithIdNotExistException(command.getHostId()));
        hamsterForUpdate.setHost(host);
        hamsterForUpdate.setStartOfFostering(command.getStartOfFoster());

        hamsterRepository.save(hamsterForUpdate);

        return hamsterMapper.toDtoWithoutAdoptive(hamsterForUpdate);
    }

    public HamsterDto adoptHamster(long id, AdoptHamsterCommand command) {
        Hamster adoptedHamster = findHamsterEntityById(id);
        Adoptive adoptive = adoptiveRepository.findAdoptiveByIdWithHamsters(command.getAdoptiveId());

        adoptedHamster.setHamsterStatus(HamsterStatus.ADOPTED);
        adoptedHamster.setAdoptive(adoptive);
        adoptedHamster.setDateOfAdoption(command.getDateOfAdoption());

        adoptive.addHamster(adoptedHamster);
        hamsterRepository.save(adoptedHamster);

        return hamsterMapper.toDto(adoptedHamster);
    }


    private List<Hamster> findHamsterWithNamePart(String hamsterName) {
        List<Hamster> result = hamsterRepository.findHamsterByNameContains(hamsterName);
        if (result.isEmpty()) {
            throw new HamsterWithNameNotExist(hamsterName);
        }
        return result;
    }

    private Hamster findHamsterEntityById(long id) {
        return hamsterRepository.findById(id)
                .orElseThrow(() -> new HamsterWithIdNotExistException(id));
    }

    private Host checkHostIsAvailable(long id) {
        Host host = hostRepository.findById(id).orElseThrow(
                () -> new HostWithIdNotExistException(id));

        if (host.getHostStatus().equals(HostStatus.INACTIVE)) {
            throw new HostIsInactiveException(id);
        }

        List<Hamster> fosteringHamstersByHost = hamsterRepository.findFosteringHamstersByHostId(host.getId());
        if (fosteringHamstersByHost.size() >= host.getCapacity()) {
            throw new HostCantTakeMoreHamstersException(host.getId());
        }
        return host;
    }

    private HamsterSpecies findHamsterSpecies(String hamsterSpecies) {
        for (HamsterSpecies sp : HamsterSpecies.values()) {
            if (sp.getNameOfSpecies().equals(hamsterSpecies)) {
                return sp;
            }
        }
        throw new HamsterSpeciesNotExistException(hamsterSpecies);
    }

    private Gender findGender(String gender) {
        for (Gender g : Gender.values()) {
            if (g.getGender().equals(gender)) {
                return g;
            }
        }
        throw new HamsterGenderNotAcceptableException(gender);
    }
    private HamsterStatus findHamsterStatus(String status) {
        for (HamsterStatus s : HamsterStatus.values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        throw new HamsterStatusNotAcceptableException(status);
    }

}
