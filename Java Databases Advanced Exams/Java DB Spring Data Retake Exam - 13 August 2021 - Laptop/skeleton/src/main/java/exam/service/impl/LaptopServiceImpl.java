package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopSeedDTO;
import exam.model.entity.Laptop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class LaptopServiceImpl implements LaptopService {

    private static final String LAPTOP_FILE_PATH = "skeleton/src/main/resources/files/json/laptops.json";

    private final LaptopRepository laptopRepository;
    private final ShopService shopService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopService shopService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOP_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedDTO[].class))
                .filter(laptopSeedDTO -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDTO);
                    builder.append(isValid ? String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                            laptopSeedDTO.getMacAddress(),
                            laptopSeedDTO.getCpuSpeed(),
                            laptopSeedDTO.getRam(),
                            laptopSeedDTO.getStorage())
                            : "Invalid Laptop")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(laptopSeedDTO -> {
                    Laptop laptop = modelMapper.map(laptopSeedDTO, Laptop.class);
                    laptop.setShop(shopService.findByName(laptopSeedDTO.getShop().getName()));
                    return laptop;
                })
                .forEach(laptopRepository::save);

        return builder.toString();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder builder = new StringBuilder();

        laptopRepository.findBestLaptopOrderByCpuSpeedDescRamDescStorageDescMacAddress()
                .forEach(laptop -> {
                    builder.append(String.format("Laptop - %s\n" +
                            "*Cpu speed - %.2f\n" +
                            "**Ram - %d\n" +
                            "***Storage - %d\n" +
                            "****Price - %.2f\n" +
                            "#Shop name - %s\n" +
                            "##Town - %s\n",
                            laptop.getMacAddress(),
                            laptop.getCpuSpeed(),
                            laptop.getRam(),
                            laptop.getStorage(),
                            laptop.getPrice(),
                            laptop.getShop().getName(),
                            laptop.getShop().getTown().getName()))
                            .append(System.lineSeparator());
                });

        return builder.toString();
    }
}
