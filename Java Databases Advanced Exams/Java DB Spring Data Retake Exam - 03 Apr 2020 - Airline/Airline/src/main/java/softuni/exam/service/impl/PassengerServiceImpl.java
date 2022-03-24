package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PassengerSeedDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String PASSENGER_FILE_PATH = "src/main/resources/files/json/passengers.json";

    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TownService townService;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TownService townService) {
        this.passengerRepository = passengerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.townService = townService;
    }


    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGER_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(gson.fromJson(readPassengersFileContent(), PassengerSeedDTO[].class))
                .filter(passengerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(passengerSeedDTO);

                    builder.append(isValid ? String.format("Successfully imported Passenger %s - %s",
                            passengerSeedDTO.getLastName(),
                            passengerSeedDTO.getEmail())
                            : "Invalid Passenger")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(passengerSeedDTO -> {
                   Passenger passenger = modelMapper.map(passengerSeedDTO, Passenger.class);
                    passenger.setTown(townService.findByName(passengerSeedDTO.getTown()));
                    return passenger;
                })
                .forEach(passengerRepository::save);
        return builder.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder builder = new StringBuilder();
         passengerRepository.findAllByPassengerOrderByCountTicketsDesc()
                .forEach(passenger -> {
                   builder.append(String.format("Passenger %s  %s\n" +
                                    "\tEmail - %s\n" +
                                    "Phone - %s\n" +
                                    "\tNumber of tickets - %d\n",
                            passenger.getFirstName(), passenger.getLastName(),
                            passenger.getEmail(),
                            passenger.getPhoneNumber(),
                            passenger.getTickets().size()))
                           .append(System.lineSeparator());
                });
         return builder.toString();
    }

    @Override
    public Passenger findByEmail(String email) {

        return passengerRepository.findByEmail(email);
    }
}
