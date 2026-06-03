package dbataev.nextcode.model.dto;

import dbataev.nextcode.enums.LessonState;
import lombok.Data;

@Data
public class LessonDto {
    private Long id;
    private String title;
    private Integer xpReward;
    private String theoryText;
    private LessonState state;
}