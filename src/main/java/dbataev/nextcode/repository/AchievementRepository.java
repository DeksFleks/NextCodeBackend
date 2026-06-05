package dbataev.nextcode.repository;

import dbataev.nextcode.enums.AchievementType;
import dbataev.nextcode.model.base.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByType(AchievementType type);

}
