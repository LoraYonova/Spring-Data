package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForestSeedRootDTO;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ForecastServiceImpl implements ForecastService {

    private static final String FORECAST_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";

    private final ForecastRepository forecastRepository;
    private final CityService cityService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public ForecastServiceImpl(ForecastRepository forecastRepository, CityService cityService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.forecastRepository = forecastRepository;
        this.cityService = cityService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECAST_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(FORECAST_FILE_PATH, ForestSeedRootDTO.class)
                        .getForecast()
                                .stream().filter(forestSeedDTO -> {
                    boolean isValid = validationUtil.isValid(forestSeedDTO) && !isEntityExist(forestSeedDTO.getDaysOfWeek(),
                            forestSeedDTO.getMaxTemperature(),forestSeedDTO.getMinTemperature());

                    builder.append(isValid ? String.format("Successfully import forecast %s - %.2f",
                            forestSeedDTO.getDaysOfWeek(), forestSeedDTO.getMaxTemperature())
                            : "Invalid forecast")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(forestSeedDTO -> {
                    Forecast forecast = modelMapper.map(forestSeedDTO, Forecast.class);
                    forecast.setCity(cityService.findById(forestSeedDTO.getCity()));
                    return forecast;
                })
                .forEach(forecastRepository::save);

        return builder.toString();
    }

    private boolean isEntityExist(DaysOfWeek daysOfWeek, Double maxTemperature, Double minTemperature) {
        return forecastRepository.existsByDaysOfWeekAndMaxTemperatureAndMinTemperature(daysOfWeek, maxTemperature, minTemperature);
    }


    @Override
    public String exportForecasts() {
        StringBuilder builder = new StringBuilder();

        DaysOfWeek sunday = DaysOfWeek.SUNDAY;
        Integer citizen = 150000;

        forecastRepository.findAllSundayForecastWithLessThan150000citizens(sunday, citizen)
                .forEach(forecast -> {
                    builder.append(String.format("City: %s:\n" +
                            "   \t\t-min temperature: %.2f\n" +
                            "   \t\t--max temperature: %.2f\n" +
                            "   \t\t---sunrise: %s\n" +
                            "----sunset: %s\n",
                            forecast.getCity().getCityName(),
                            forecast.getMinTemperature(),
                            forecast.getMaxTemperature(),
                            forecast.getSunrise(),
                            forecast.getSunset()));
                });
        return builder.toString();
    }
}
