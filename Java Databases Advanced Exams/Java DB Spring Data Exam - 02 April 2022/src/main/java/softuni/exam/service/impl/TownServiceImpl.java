package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownSeedDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";

    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public TownServiceImpl(TownRepository townRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readTownsFileContent(), TownSeedDTO[].class))
                .filter(townSeedDTO -> {
                    boolean isValid = validationUtil.isValid(townSeedDTO);
                    builder.append(isValid ? String.format("Successfully imported town %s - %d",
                            townSeedDTO.getTownName(), townSeedDTO.getPopulation())
                            : "Invalid town")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(townSeedDTO -> modelMapper.map(townSeedDTO, Town.class))
                .forEach(townRepository::save);

        return builder.toString();
    }

    @Override
    public Town findByTownName(String town) {
        return townRepository.findByTownName(town);
    }


}
