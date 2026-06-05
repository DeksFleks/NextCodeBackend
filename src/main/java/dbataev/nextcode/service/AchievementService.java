package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Achievement;
import dbataev.nextcode.model.base.User;
import dbataev.nextcode.model.dto.AchievementDto;
import dbataev.nextcode.model.manyToMany.UserAchievements;
import dbataev.nextcode.model.mapper.AchievementMapper;
import dbataev.nextcode.repository.AchievementRepository;
import dbataev.nextcode.repository.UserAchievementRepository;
import org.springframework.stereotype.Service;
import dbataev.nextcode.enums.AchievementType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    public AchievementService(AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    public List<AchievementDto> getAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementDto> achievementDtos = new ArrayList<>();

        for(Achievement achievement : achievements) {
            achievementDtos.add(AchievementMapper.toDto(achievement));
        }

        return achievementDtos;
    }

    public List<AchievementDto> checkAchievements(User user, AchievementType type) {
        List<Achievement> achievements = achievementRepository.findByType(type);
        List<AchievementDto> achievementDtos = new ArrayList<>();

        for (Achievement achievement : achievements) {
            if (userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) continue;
            else {
                if (type == AchievementType.LESSONS_COMPLETED) {
                    Integer conditionValue = achievement.getConditionValue();
                    Integer currentValue = user.getCompletedLessons();

                    if (currentValue >= conditionValue) {
                        UserAchievements userAchievements = new UserAchievements();

                        userAchievements.setUser(user);
                        userAchievements.setAchievement(achievement);
                        userAchievements.setAchievedAt(LocalDateTime.now());

                        achievementDtos.add(AchievementMapper.toDto(achievement));

                        userAchievementRepository.save(userAchievements);
                    }
                }
            }
        }

        return achievementDtos;
    }

    public List<AchievementDto> checkAllAchievements(User user) {
        List<AchievementDto> achievementDtos = new ArrayList<>();

        for (AchievementType type : AchievementType.values()) {
            achievementDtos.addAll(checkAchievements(user, type));
        }

        return achievementDtos;
    }

    private Long getCurrentValueByType(User user, AchievementType type) {
        return switch (type){
            case LESSONS_COMPLETED -> user.getCompletedLessons().longValue();
            case COURSES_COMPLETED -> null;
            case XP_EARNED -> null;
            case LEVEL_REACHED -> null;
            case STREAK_DAYS -> null;
            case DAILY_COMPLETED -> null;
            case TASKS_COMPLETED -> null;
        };
    }
}
