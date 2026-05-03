package dbataev.nextcode.model.dto;

import dbataev.nextcode.LessonState;
import lombok.Data;

import java.util.List;

@Data
public class LessonDto {
    private Long id;
    private String title;
    private Integer xpReward;
    private String theoryText;
    private LessonState state;
}