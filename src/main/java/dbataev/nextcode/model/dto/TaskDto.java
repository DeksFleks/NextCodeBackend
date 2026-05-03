package dbataev.nextcode.model.dto;

import dbataev.nextcode.TaskType;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private TaskType type;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String explanation;
}
