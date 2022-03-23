package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Car;
import softuni.exam.models.Picture;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c " +
            "ORDER BY size(c.pictures) DESC , c.make")
    List<Car> findAllCarsOrderByPicturesCountDescThenByMake();

}
