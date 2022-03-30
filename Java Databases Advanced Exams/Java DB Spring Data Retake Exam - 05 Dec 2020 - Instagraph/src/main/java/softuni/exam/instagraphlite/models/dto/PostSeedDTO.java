package softuni.exam.instagraphlite.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public class PostSeedDTO {

    @XmlElement
    @NotBlank
    @Size(min = 21)
    private String caption;

    @XmlElement(name = "user")
    @NotNull
    private UserSeedUsernameDTO username;

    @XmlElement(name = "picture")
    @NotNull
    private PictureSeedPathDTO pictures;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public UserSeedUsernameDTO getUsername() {
        return username;
    }

    public void setUsername(UserSeedUsernameDTO username) {
        this.username = username;
    }

    public PictureSeedPathDTO getPictures() {
        return pictures;
    }

    public void setPictures(PictureSeedPathDTO pictures) {
        this.pictures = pictures;
    }
}
