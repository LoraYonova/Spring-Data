package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApartmentSeedRootDTO {

    @XmlElement(name = "apartment")
    private List<ApartmentSeedDTO> apartment;

    public List<ApartmentSeedDTO> getApartment() {
        return apartment;
    }

    public void setApartment(List<ApartmentSeedDTO> apartment) {
        this.apartment = apartment;
    }
}
