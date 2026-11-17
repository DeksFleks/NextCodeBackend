package dbataev.nextcode.model.mapper;
import dbataev.nextcode.enums.LessonState;
import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.model.content.LessonTranslation;
import dbataev.nextcode.model.dto.LessonDto;
public class LessonMapper {
    public static LessonDto toDto(Lesson lesson, LessonState state) { return toDto(lesson, state, 1L); }
    public static LessonDto toDto(Lesson lesson, LessonState state, Long languageId) {
        var text = ContentTranslations.resolve(lesson.getTranslations(), LessonTranslation::getLanguageId, languageId);
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId()); dto.setTitle(text.getTitle()); dto.setTheoryText(text.getTheoryText());
        dto.setXpReward(lesson.getXpReward()); dto.setState(state); return dto;
    }
}
