package hospital;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class Visited extends HospitalEntity {
    private LocalDate date;
    private String comments;
    private Set<Patients> patient;

    public Visited() {
    }


    @Column(name = "date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
