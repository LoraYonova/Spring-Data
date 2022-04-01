package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatSeedRootDTO {

    @XmlElement(name = "stat")
    private List<StatSeedDTO> stat;

    public List<StatSeedDTO> getStat() {
        return stat;
    }

    public void setStat(List<StatSeedDTO> stat) {
        this.stat = stat;
    }
}
