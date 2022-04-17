package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    boolean existsByDaysOfWeekAndMaxTemperatureAndMinTemperature(DaysOfWeek daysOfWeek, Double maxTemperature, Double minTemperature);

    @Query("SELECT f FROM Forecast f " +
            "WHERE f.daysOfWeek = :sunday AND f.city.population < :citizen " +
            "ORDER BY f.maxTemperature DESC, f.id ASC ")
    List<Forecast> findAllSundayForecastWithLessThan150000citizens(DaysOfWeek sunday, Integer citizen);


}
