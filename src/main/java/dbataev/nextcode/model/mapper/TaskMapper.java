package dbataev.nextcode.model.mapper;
import dbataev.nextcode.enums.TaskType;
import dbataev.nextcode.model.base.Task;
import dbataev.nextcode.model.content.*;
import dbataev.nextcode.model.dto.TaskDto;
public class TaskMapper {
    public static TaskDto toDto(Task task, Long languageId) {
        var dto = new TaskDto();
        dto.setId(task.getId()); dto.setType(TaskType.valueOf(task.getType()));
        dto.setQuestion(ContentTranslations.resolve(task.getTranslations(), TaskTranslation::getLanguageId, languageId).getQuestion());
        switch(dto.getType()) {
            case MULTIPLE_CHOICE -> {
                var choice = task.getMultipleChoice();
                dto.setMultipleSelect(Boolean.TRUE.equals(choice.getIsMultipleSelect()));
                dto.setOptions(choice.getOptions().stream().map(o -> new TaskDto.OptionDto(o.getId(),
                    ContentTranslations.resolve(o.getTranslations(), OptionTranslation::getLanguageId, languageId).getOptionText())).toList());
            }
            case CODING -> {
                dto.setStarterCode(task.getCoding().getStarterCode());
                dto.setProgrammingLanguage(task.getCoding().getProgrammingLanguage());
            }
            case FILL_BLANK -> {
                dto.setSentenceBefore(task.getFillBlank().getSentenceBefore());
                dto.setSentenceAfter(task.getFillBlank().getSentenceAfter());
            }
        }
        return dto;
    }
}
