package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDTO;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final OfferRepository offerRepository;
    private final AgentService agentService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public OfferServiceImpl(OfferRepository offerRepository, AgentService agentService, ApartmentService apartmentService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.offerRepository = offerRepository;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDTO.class)
                .getOffer()
                .stream()
                .filter(offerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(offerSeedDTO)
                            && agentService.isEntityExist(offerSeedDTO.getAgent().getFirstName())
                            && isEntityExist(offerSeedDTO.getApartment().getId());
                    builder.append(isValid ? String.format("Successfully imported offer %.2f",
                                    offerSeedDTO.getPrice())
                                    : "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(offerSeedDTO -> {
                    Offer offer = modelMapper.map(offerSeedDTO, Offer.class);
                    offer.setAgent(agentService.findByFirstName(offerSeedDTO.getAgent().getFirstName()));
                    return offer;
                })
                .forEach(offerRepository::save);

        return builder.toString();
    }

    private boolean isEntityExist(Long id) {
        return apartmentService.findById(id);
    }


    @Override
    public String exportOffers() {
        StringBuilder builder = new StringBuilder();
        ApartmentType three_rooms = ApartmentType.valueOf("three_rooms");

        offerRepository.findBestOffers(three_rooms)
                .forEach(offer -> {
                    builder.append(String.format("Agent %s %s with offer №%d:\n" +
                                    "   \t\t-Apartment area: %.2f\n" +
                                    "   \t\t--Town: %s\n" +
                                    "   \t\t---Price: %.2f$\n",
                            offer.getAgent().getFirstName(), offer.getAgent().getLastName(), offer.getId(),
                            offer.getApartment().getArea(),
                            offer.getApartment().getTown().getTownName(),
                            offer.getPrice()));
                });

        return builder.toString();
    }
}
