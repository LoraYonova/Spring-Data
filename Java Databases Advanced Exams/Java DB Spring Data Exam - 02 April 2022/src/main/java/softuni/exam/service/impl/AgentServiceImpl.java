package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public AgentServiceImpl(AgentRepository agentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.agentRepository = agentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentSeedDTO[].class))
                .filter(agentSeedDTO -> {
                    boolean isValid = validationUtil.isValid(agentSeedDTO) && !isEntityExist(agentSeedDTO.getFirstName());
                    builder.append(isValid ? String.format("Successfully imported agent - %s %s",
                            agentSeedDTO.getFirstName(), agentSeedDTO.getLastName())
                            : "Invalid agent")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(agentSeedDTO -> {
                    Agent agent = modelMapper.map(agentSeedDTO, Agent.class);
                    agent.setTown(townService.findByTownName(agentSeedDTO.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);



        return builder.toString();
    }

    @Override
    public Agent findByFirstName(String firstName) {

        return agentRepository.findByFirstName(firstName);
    }

    @Override
    public boolean isEntityExist(String firstName) {
        return agentRepository.existsByFirstName(firstName);
    }


}
