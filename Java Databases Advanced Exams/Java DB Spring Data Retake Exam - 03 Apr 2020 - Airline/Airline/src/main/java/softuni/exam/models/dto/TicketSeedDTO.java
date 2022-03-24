package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDTO {

    @XmlElement(name = "serial-number")
    @Size(min = 2)
    private String serialNumber;

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement(name = "take-off")
    private String takeoff;

    @XmlElement(name = "from-town")
    private FromTownDTO fromTown;

    @XmlElement(name = "to-town")
    private ToTownDTO toTown;

    @XmlElement(name = "passenger")
    private PassengerEmailDTO passenger;

    @XmlElement(name = "plane")
    private PlaneRegisterNumberDTO plane;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownDTO getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownDTO fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownDTO getToTown() {
        return toTown;
    }

    public void setToTown(ToTownDTO toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDTO passenger) {
        this.passenger = passenger;
    }

    public PlaneRegisterNumberDTO getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumberDTO plane) {
        this.plane = plane;
    }
}
