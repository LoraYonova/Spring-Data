package hospital;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patients extends HospitalEntity {

    private String firstName;
    private String lastName;
    private String addresses;
    private String email;
    private LocalDate dateOfBirth;
    private String picture;
    private Boolean medicalInsurance;
    private Set<Visited> visited;
    private Set<Diagnoses> diagnoses;
    private Set<Medicament> medicament;

    public Patients() {
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "addresses", nullable = false)
    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "date_of_birth")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "picture", columnDefinition = "BLOB")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column(name = "medical_insurance")
    public Boolean getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(Boolean medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    @ManyToMany(mappedBy = "patient")
    public Set<Visited> getVisited() {
        return visited;
    }

    public void setVisited(Set<Visited> visited) {
        this.visited = visited;
    }

    @ManyToMany(mappedBy = "patient")
    public Set<Diagnoses> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnoses> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @ManyToMany(mappedBy = "patient")
    public Set<Medicament> getMedicament() {
        return medicament;
    }

    public void setMedicament(Set<Medicament> medicament) {
        this.medicament = medicament;
    }
}
