package dbataev.nextcode.model.dto;

import dbataev.nextcode.enums.AchievementType;
import dbataev.nextcode.enums.LevelAchievementType;
import lombok.Data;

@Data
public class AchievementDto {
    private String title;
    private String description;
    private AchievementType type;
    private Integer conditionValue;
    private Integer currentValue;
    private LevelAchievementType levelType;
}
