package dbataev.nextcode.model.mapper;

import dbataev.nextcode.LessonState;
import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.dto.LessonDto;
import dbataev.nextcode.model.dto.TaskDto;

import java.util.List;

public class LessonMapper {
    public static LessonDto toDto(Lesson lesson, LessonState state) {
        LessonDto dto = new LessonDto();

        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setXpReward(lesson.getXpReward());
        dto.setTheoryText(lesson.getTheoryText());
        dto.setState(state);

        return dto;
    }
}
