package dbataev.nextcode.model.dto;
import dbataev.nextcode.enums.TaskType;
import lombok.Data;
import java.util.List;
@Data
public class TaskDto {
    private Long id;
    private TaskType type;
    private String question;
    private List<OptionDto> options;
    private boolean multipleSelect;
    private String starterCode;
    private String programmingLanguage;
    private String sentenceBefore;
    private String sentenceAfter;
    public record OptionDto(Long id, String text) {}
}
