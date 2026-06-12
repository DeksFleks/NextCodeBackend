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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        for (Achievement achievement : achievements) {
            achievementDtos.add(AchievementMapper.toDto(achievement));
        }

        return achievementDtos;
    }

    public List<AchievementDto> checkAchievements(User user, AchievementType type) {
        Long currentValue = getCurrentValueByType(user, type);

        if (currentValue == null) {
            return List.of();
        }

        List<Achievement> achievements = achievementRepository.findByType(type);

        Set<Long> receivedAchievementIds = new HashSet<>(
                userAchievementRepository.findAchievementIdsByUserIdAndType(user.getId(), type)
        );

        List<UserAchievements> achievementsToSave = new ArrayList<>();
        List<AchievementDto> achievementDtos = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Achievement achievement : achievements) {
            if (receivedAchievementIds.contains(achievement.getId())) {
                continue;
            }

            Integer conditionValue = achievement.getConditionValue();

            if (conditionValue != null && currentValue >= conditionValue) {
                UserAchievements userAchievement = new UserAchievements();

                userAchievement.setUser(user);
                userAchievement.setAchievement(achievement);
                userAchievement.setAchievedAt(now);

                achievementsToSave.add(userAchievement);
                achievementDtos.add(AchievementMapper.toDto(achievement));
            }
        }

        if (!achievementsToSave.isEmpty()) {
            userAchievementRepository.saveAll(achievementsToSave);
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
        return switch (type) {
            case LESSONS_COMPLETED -> user.getCompletedLessons().longValue();
            case COURSES_COMPLETED -> null;
            case XP_EARNED -> null;
            case LEVEL_REACHED -> user.getLevel().longValue();
            case STREAK_DAYS -> user.getBestStreak().longValue();
            case DAILY_COMPLETED -> null;
            case TASKS_COMPLETED -> null;
        };
    }
}