package dbataev.nextcode.repository;

import dbataev.nextcode.enums.AchievementType;
import dbataev.nextcode.model.manyToMany.UserAchievements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievements, Long> {

    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);

    @Query("""
    SELECT ua.achievement.id
    FROM user_achievements ua
    WHERE ua.user.id = :userId
    AND ua.achievement.type = :type
""")
    List<Long> findAchievementIdsByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") AchievementType type
    );
}
