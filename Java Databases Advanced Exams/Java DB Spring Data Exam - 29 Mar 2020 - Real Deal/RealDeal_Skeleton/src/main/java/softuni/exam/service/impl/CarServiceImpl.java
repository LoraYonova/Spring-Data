package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.dto.CarSeedDTO;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(gson.fromJson(readCarsFileContent(), CarSeedDTO[].class))
                .filter(carSeedDTO -> {
                    boolean isValid = validationUtil.isValid(carSeedDTO);

                    stringBuilder.append(isValid ? String.format("Successfully imported car - %s - %s",
                            carSeedDTO.getMake(), carSeedDTO.getModel())
                            : "Invalid car")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(carSeedDTO -> modelMapper.map(carSeedDTO, Car.class))
                .forEach(carRepository::save);


        return stringBuilder.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder builder = new StringBuilder();
        carRepository.findAllCarsOrderByPicturesCountDescThenByMake()
                .forEach(car -> {
                    builder.append(String.format("Car make - %s, model - %s\n" +
                            "\tKilometers - %d\n" +
                            "\tRegistered on - %s\n" +
                            "\tNumber of pictures - %d\n",
                            car.getMake(), car.getModel(),
                            car.getKilometers(),
                            car.getRegisteredOn(),
                            car.getPictures().size()))
                            .append(System.lineSeparator());
                });
        return builder.toString();
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElse(null);
    }
}
