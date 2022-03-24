package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlaneSeedRootDTO;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {

    private static final String PLANE_FILE_PATH = "src/main/resources/files/xml/planes.xml";

    private final PlaneRepository planeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public PlaneServiceImpl(PlaneRepository planeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.planeRepository = planeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANE_FILE_PATH));
    }

    @Override
    public String importPlanes() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(PLANE_FILE_PATH, PlaneSeedRootDTO.class)
                .getPlanes()
                .stream()
                .filter(planeSeedDTO -> {
                    boolean isValid = validationUtil.isValid(planeSeedDTO);

                    builder.append(isValid ? String.format("Successfully imported Plane %s",
                            planeSeedDTO.getRegisterNumber())
                            : "Invalid Plane")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(planeSeedDTO -> modelMapper.map(planeSeedDTO, Plane.class))
                .forEach(planeRepository::save);

        return builder.toString();
    }

    @Override
    public Plane findByRegisterNumber(String registerNumber) {

        return planeRepository.findByRegisterNumber(registerNumber);
    }
}
