package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.Seller;
import softuni.exam.models.dto.SellerSeedRootDTO;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String SELLER_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public SellerServiceImpl(SellerRepository sellerRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLER_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {

        StringBuilder stringBuilder = new StringBuilder();

        xmlParser
                .fromFile(SELLER_FILE_PATH, SellerSeedRootDTO.class)
                .getSellers()
                .stream()
                .filter(sellerSeedDTO -> {
                    boolean isValid = validationUtil.isValid(sellerSeedDTO);
                    stringBuilder.append(isValid ? String.format("Successfully import seller %s - %s",
                            sellerSeedDTO.getLastName(),
                            sellerSeedDTO.getEmail())
                            : "Invalid seller")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(sellerSeedDTO -> modelMapper.map(sellerSeedDTO, Seller.class))
                .forEach(sellerRepository::save);



        return stringBuilder.toString();
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }
}
