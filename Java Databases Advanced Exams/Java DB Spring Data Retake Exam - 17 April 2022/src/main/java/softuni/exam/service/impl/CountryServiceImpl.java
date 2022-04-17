package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String COUNTRY_FILE_PATH = "src/main/resources/files/json/countries.json";

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRY_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readCountriesFromFile(), CountrySeedDTO[].class))
                .filter(countrySeedDTO -> {
                    boolean isValid = validationUtil.isValid(countrySeedDTO) && !isEntityExist(countrySeedDTO.getCountryName());
                    builder.append(isValid ? String.format("Successfully imported country %s - %s",
                                    countrySeedDTO.getCountryName(), countrySeedDTO.getCurrency())
                            : "Invalid country")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(countrySeedDTO -> modelMapper.map(countrySeedDTO, Country.class))
                .forEach(countryRepository::save);

        return builder.toString();
    }

    @Override
    public boolean isEntityExist(String countryName) {
        return countryRepository.existsByCountryName(countryName);
    }

    @Override
    public Country findById(Long country) {
        return countryRepository.findById(country).orElse(null);
    }
}
