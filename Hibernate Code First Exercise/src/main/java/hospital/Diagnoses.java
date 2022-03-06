package hospital;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class Diagnoses extends HospitalEntity{
    private String name;
    private String comments;
    private Set<Patients> patient;

    public Diagnoses() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "comments", columnDefinition = "TEXT")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    @ManyToMany
    public Set<Patients> getPatient() {
        return patient;
    }

    public void setPatient(Set<Patients> patient) {
        this.patient = patient;
    }
}
