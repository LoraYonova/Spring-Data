package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureSeedDTO;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURE_FILE_PATH = "src/main/resources/files/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURE_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readFromFileContent(), PictureSeedDTO[].class))
                .filter(pictureSeedDTO -> {
                    boolean isValid = validationUtil.isValid(pictureSeedDTO) && !isEntityExist(pictureSeedDTO.getPath());
                    builder.append(isValid ? String.format("Successfully imported Picture, with size %.2f",
                            pictureSeedDTO.getSize())
                            : "Invalid Picture")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(pictureSeedDTO -> modelMapper.map(pictureSeedDTO, Picture.class))
                .forEach(pictureRepository::save);

        return builder.toString();
    }

    @Override
    public boolean isEntityExist(String path) {
        return pictureRepository.existsByPath(path);
    }

    @Override
    public String exportPictures() {
        StringBuilder builder = new StringBuilder();

        pictureRepository.findAllBySizeGreaterThanOrderBySize(30000D)
                .forEach(picture -> {
                    builder.append(String.format("%.2f - %s",
                            picture.getSize(), picture.getPath()))
                            .append(System.lineSeparator());
                });

        return builder.toString();
    }

    @Override
    public Picture findByPath(String profilePicture) {
        return pictureRepository.findByPath(profilePicture).orElse(null);
    }


}
