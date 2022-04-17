package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDTO;
import softuni.exam.models.entity.City;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {
    private static final String CITY_FILE_PATH = "src/main/resources/files/json/cities.json";

    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CityServiceImpl(CityRepository cityRepository, CountryService countryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITY_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readCitiesFileContent(), CitySeedDTO[].class))
                .filter(citySeedDTO -> {
                    boolean isValid = validationUtil.isValid(citySeedDTO) && !isEntityExist(citySeedDTO.getCityName());
                    builder.append(isValid ? String.format("Successfully imported city %s - %d",
                            citySeedDTO.getCityName(), citySeedDTO.getPopulation())
                            : "Invalid city")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(citySeedDTO -> {
                    City city = modelMapper.map(citySeedDTO, City.class);
                    city.setCountry(countryService.findById(citySeedDTO.getCountry()));
                    return city;
                })
                .forEach(cityRepository::save);

        return builder.toString();
    }

    @Override
    public City findById(Long city) {
        return cityRepository.findById(city).orElse(null);
    }


    private boolean isEntityExist(String cityName) {
        return cityRepository.existsByCityName(cityName);
    }

}
