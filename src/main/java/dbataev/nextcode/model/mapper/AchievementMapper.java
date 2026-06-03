package dbataev.nextcode.model.mapper;

import dbataev.nextcode.model.base.Achievement;
import dbataev.nextcode.model.dto.AchievementDto;

public class AchievementMapper {
    public static AchievementDto toDto(Achievement achievement) {
        AchievementDto dto = new AchievementDto();

        dto.setTitle(achievement.getTitle());
        dto.setDescription(achievement.getDescription());
        dto.setType(achievement.getType());
        dto.setConditionValue(achievement.getConditionValue());

        return dto;
    }
}
