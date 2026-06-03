package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Achievement;
import dbataev.nextcode.model.dto.AchievementDto;
import dbataev.nextcode.model.mapper.AchievementMapper;
import dbataev.nextcode.repository.AchievementRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    public List<AchievementDto> getAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementDto> achievementDtos = new ArrayList<>();

        for(Achievement achievement : achievements) {
            achievementDtos.add(AchievementMapper.toDto(achievement));
        }

        return achievementDtos;
    }
}
