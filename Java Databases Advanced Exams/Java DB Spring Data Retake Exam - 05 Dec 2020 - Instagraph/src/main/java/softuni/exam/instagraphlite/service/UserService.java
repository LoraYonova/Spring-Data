package softuni.exam.instagraphlite.service;

import softuni.exam.instagraphlite.models.dto.UserSeedUsernameDTO;
import softuni.exam.instagraphlite.models.entity.User;

import java.io.IOException;

public interface UserService {
    boolean areImported();
    String readFromFileContent() throws IOException;
    String importUsers() throws IOException;

    boolean isEntityExist(String username);

    String exportUsersWithTheirPosts();


    User findByUsername(String username);
}
