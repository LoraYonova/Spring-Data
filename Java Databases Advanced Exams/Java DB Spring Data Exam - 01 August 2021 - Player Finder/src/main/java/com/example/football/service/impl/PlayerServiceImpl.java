package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDTO.class)
                .getPlayer()
                .stream()
                .filter(playerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(playerSeedDTO);
                    builder.append(isValid ? String.format("Successfully imported Player %s %s - %s",
                            playerSeedDTO.getFirstName(), playerSeedDTO.getLastName(), playerSeedDTO.getPosition())
                            : "Invalid Player")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(playerSeedDTO -> {
                    Player player = modelMapper.map(playerSeedDTO, Player.class);
                    player.setTown(townService.findByName(playerSeedDTO.getTown().getName()));
                    player.setTeam(teamService.findByName(playerSeedDTO.getTeam().getName()));
                    return player;
                })
                .forEach(playerRepository::save);


        return builder.toString();
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder builder = new StringBuilder();
        playerRepository.findTheBestPlayers()
                .forEach(player -> {
                    builder.append(String.format("Player - %s %s\n" +
                            "\tPosition - %s\n" +
                            "Team - %s\n" +
                            "\tStadium - %s\n",
                            player.getFirstName(), player.getLastName(),
                            player.getPosition().name(),
                            player.getTeam().getName(),
                            player.getTeam().getStadiumName()));
                });

        return builder.toString();
    }
}
