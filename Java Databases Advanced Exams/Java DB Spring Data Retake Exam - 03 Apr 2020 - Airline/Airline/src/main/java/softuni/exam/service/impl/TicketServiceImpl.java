package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketSeedRootDTO;
import softuni.exam.models.entity.Ticket;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_FILE_PATH = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final TownService townService;
    private final PlaneService planeService;
    private final PassengerService passengerService;


    public TicketServiceImpl(TicketRepository ticketRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser, TownService townService, PlaneService planeService, PassengerService passengerService) {
        this.ticketRepository = ticketRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.townService = townService;
        this.planeService = planeService;
        this.passengerService = passengerService;
    }


    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKET_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, IOException {
        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(TICKET_FILE_PATH, TicketSeedRootDTO.class)
                .getTickets().stream()
                .filter(ticketSeedRootDTO -> {
                    boolean isValid = validationUtil.isValid(ticketSeedRootDTO);
                    builder.append(isValid ? String.format("Successfully imported Ticket %s - %s",
                            ticketSeedRootDTO.getFromTown().getName(),
                            ticketSeedRootDTO.getToTown().getName())
                    : "Invalid Ticket")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(ticketSeedRootDTO -> {
                    Ticket ticket = modelMapper.map(ticketSeedRootDTO, Ticket.class);
                    ticket.setFromTown(townService.findByName(ticketSeedRootDTO.getFromTown().getName()));
                    ticket.setToTown(townService.findByName(ticketSeedRootDTO.getToTown().getName()));
                    ticket.setPassenger(passengerService.findByEmail(ticketSeedRootDTO.getPassenger().getEmail()));
                    ticket.setPlane(planeService.findByRegisterNumber(ticketSeedRootDTO.getPlane().getRegisterNumber()));
                    return ticket;
                })
                .forEach(ticketRepository::save);


        return builder.toString();
    }
}
