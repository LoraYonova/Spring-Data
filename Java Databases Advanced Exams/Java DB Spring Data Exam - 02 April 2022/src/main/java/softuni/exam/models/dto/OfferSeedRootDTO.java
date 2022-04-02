package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDTO {

    @XmlElement(name = "offer")
    private List<OfferSeedDTO> offer;


    public List<OfferSeedDTO> getOffer() {
        return offer;
    }

    public void setOffer(List<OfferSeedDTO> offer) {
        this.offer = offer;
    }
}
