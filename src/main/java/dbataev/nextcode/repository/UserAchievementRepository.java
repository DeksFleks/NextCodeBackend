package dbataev.nextcode.repository;

import dbataev.nextcode.model.manyToMany.UserAchievements;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievements, Long> {

    boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}
