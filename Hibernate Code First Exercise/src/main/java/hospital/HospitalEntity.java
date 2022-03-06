package hospital;

import javax.persistence.*;


@MappedSuperclass
public abstract class HospitalEntity {
    private Long id;

    protected HospitalEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
