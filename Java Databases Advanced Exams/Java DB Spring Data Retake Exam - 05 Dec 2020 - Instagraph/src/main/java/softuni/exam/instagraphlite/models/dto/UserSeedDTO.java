package softuni.exam.instagraphlite.models.dto;

import com.google.gson.annotations.Expose;
import softuni.exam.instagraphlite.models.entity.Picture;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserSeedDTO {

    @Expose
    @Size(min = 2, max = 18)
    @NotBlank
    private String username;

    @Expose
    @Size(min = 4)
    private String password;

    @Expose
    @NotBlank
    private String profilePicture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
