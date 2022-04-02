package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private static final String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";

    private final ApartmentRepository apartmentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.apartmentRepository = apartmentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDTO.class)
                .getApartment()
                .stream()
                .filter(apartmentSeedDTO -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDTO) && !isEntityExist(apartmentSeedDTO.getArea());
                    builder.append(isValid ? String.format("Successfully imported apartment %s - %.2f",
                            apartmentSeedDTO.getApartmentType(), apartmentSeedDTO.getArea())
                            : "Invalid apartment")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(apartmentSeedDTO -> {
                    Apartment apartment = modelMapper.map(apartmentSeedDTO, Apartment.class);
                    apartment.setTown(townService.findByTownName(apartmentSeedDTO.getTownName()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);
        return builder.toString();
    }

    @Override
    public boolean findById(Long id) {

        return apartmentRepository.existsById(id);
    }

    private boolean isEntityExist(Double area) {
        return apartmentRepository.existsByArea(area);
    }


}
