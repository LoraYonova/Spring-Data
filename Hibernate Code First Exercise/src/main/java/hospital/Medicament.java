package hospital;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class Medicament extends HospitalEntity {

    private String name;
    private Set<Patients> patient;


    public Medicament() {
    }

    @Column(name = "mame")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    public Set<Patients> getPatient() {
        return patient;
    }

    public void setPatient(Set<Patients> patient) {
        this.patient = patient;
    }
}
