package hungarian_hamster_rescue.services;

import hungarian_hamster_rescue.dtos.adopter.AdoptHamsterCommand;
import hungarian_hamster_rescue.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_rescue.dtos.hamster.HamsterDto;
import hungarian_hamster_rescue.dtos.hamster.HamsterDtoWithoutAdopter;
import hungarian_hamster_rescue.dtos.hamster.UpdateHamsterCommand;
import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HostStatus;
import hungarian_hamster_rescue.exceptions.*;
import hungarian_hamster_rescue.mappers.HamsterMapper;
import hungarian_hamster_rescue.models.*;
import hungarian_hamster_rescue.repositories.AdopterRepository;
import hungarian_hamster_rescue.repositories.HamsterRepository;
import hungarian_hamster_rescue.repositories.HostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class HamsterService {

    private final HamsterMapper hamsterMapper;
    private final HamsterRepository hamsterRepository;
    private final HostRepository hostRepository;
    private final AdopterRepository adopterRepository;


    public List<HamsterDto> getListOfHamsters(Optional<String> hamsterName) {
        if (hamsterName.isPresent()) {
            List<Hamster> result = findHamsterWithNamePart(hamsterName.get());
            return hamsterMapper.toDto(result);
        }

        return hamsterMapper.toDto(hamsterRepository.findAll());
    }

    public List<HamsterDtoWithoutAdopter> getListOfCurrentHamsters() {
        List<Hamster> hamstersWithoutOwner = hamsterRepository.findFosteringHamsters();
        List<HamsterDtoWithoutAdopter> hamsters = hamsterMapper.toDtoWithoutAdopter(hamstersWithoutOwner);
        for (HamsterDtoWithoutAdopter h: hamsters) {
            h.setLocation(findHamsterPlace(h.getId()));
        }

        return hamsters;

    }

    public HamsterDtoWithoutAdopter findAdoptableHamsterById(long id) {
        return hamsterMapper.toDtoWithoutAdopter(findHamsterEntityById(id));
    }

    @Transactional
    public HamsterDtoWithoutAdopter createHamster(CreateHamsterCommand command) {
        Host host = checkHostIsAvailable(command.getHostId());

        Hamster hamster = Hamster.builder()
                .name(command.getName())
                .hamsterSpecies(findHamsterSpecies(command.getHamsterSpecies()))
                .color(command.getColor())
                .gender(findGender(command.getGender()))
                .dateOfBirth(command.getDateOfBirth())
                .hamsterStatus(findHamsterStatus(command.getHamsterStatus()))
                .host(host)
                .startOfFostering(command.getStartOfFoster())
                .description(command.getDescription())
                .pictures(new ArrayList<>())
                .weeklyReports(new ArrayList<>())
                .build();
        host.addHamster(hamster);
        hamsterRepository.save(hamster);

        HamsterDtoWithoutAdopter newHamster = hamsterMapper.toDtoWithoutAdopter(hamster);
        newHamster.setLocation(findHamsterPlace(newHamster.getId()));

        return newHamster;
    }


    @Transactional
    public HamsterDtoWithoutAdopter updateHamsterAllAttributes(long id, UpdateHamsterCommand command) {
        Hamster hamsterForUpdate = findHamsterEntityById(id);
        Host host = hostRepository.findById(command.getHostId())
                .orElseThrow(() -> new HostWithIdNotExistException(command.getHostId()));

        hamsterForUpdate.setName(command.getName());
        hamsterForUpdate.setHamsterSpecies(findHamsterSpecies(command.getHamsterSpecies()));
        hamsterForUpdate.setColor(command.getColor());
        hamsterForUpdate.setGender(findGender(command.getGender()));
        hamsterForUpdate.setDateOfBirth(command.getDateOfBirth());
        hamsterForUpdate.setHamsterStatus(findHamsterStatus(command.getHamsterStatus()));
        hamsterForUpdate.setHost(host);

        hamsterRepository.save(hamsterForUpdate);

        return hamsterMapper.toDtoWithoutAdopter(hamsterForUpdate);
    }

    public HamsterDto adoptHamster(long id, AdoptHamsterCommand command) {
        Hamster adoptedHamster = findHamsterEntityById(id);
        Adopter adopter = adopterRepository.findAdopterByIdWithHamsters(command.getAdopterId());

        adoptedHamster.setHamsterStatus(HamsterStatus.ADOPTED);
        adoptedHamster.setAdopter(adopter);
        adoptedHamster.setDateOfAdoption(command.getDateOfAdoption());

        adopter.addHamster(adoptedHamster);
        hamsterRepository.save(adoptedHamster);

        return hamsterMapper.toDto(adoptedHamster);
    }

    public HamsterDto findHamsterById(long id){
        Hamster hamster = findHamsterEntityById(id);

        return hamsterMapper.toDto(hamster);

    }


    public String findHamsterPlace(long id){
        Hamster hamster = findHamsterEntityById(id);
        if (hamster.getAdopter() != null) {
            return "Already adopted, not in our care.";
        }

        return hamster.getHost().getAddress().getTown();
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
