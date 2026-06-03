package dbataev.nextcode.model.dto;

import dbataev.nextcode.enums.AchievementType;
import lombok.Data;

@Data
public class AchievementDto {
    private String title;
    private String description;
    private AchievementType type;
    private Integer conditionValue;
}
