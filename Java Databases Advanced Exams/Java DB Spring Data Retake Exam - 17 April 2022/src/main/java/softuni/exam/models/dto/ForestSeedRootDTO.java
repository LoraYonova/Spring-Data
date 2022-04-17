package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "forecasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForestSeedRootDTO {

    @XmlElement(name = "forecast")
    private List<ForestSeedDTO> forecast;

    public List<ForestSeedDTO> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForestSeedDTO> forecast) {
        this.forecast = forecast;
    }
}
