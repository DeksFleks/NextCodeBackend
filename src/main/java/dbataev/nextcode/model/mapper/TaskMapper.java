package dbataev.nextcode.model.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dbataev.nextcode.enums.TaskType;
import dbataev.nextcode.model.base.Task;
import dbataev.nextcode.model.dto.TaskDto;

import java.util.List;

public class TaskMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();

        dto.setId(task.getId());

        if(task.getType().toLowerCase().equals("test")){ dto.setType(TaskType.TEST); }
        if(task.getType().toLowerCase().equals("finish")){ dto.setType(TaskType.FINISH); }
        if(task.getType().toLowerCase().equals("write")){ dto.setType(TaskType.WRITE); }

        dto.setQuestion(task.getQuestion());

        if(task.getOptions() != null){ dto.setOptions(parseOptions(task.getOptions()));}

        dto.setCorrectAnswer(task.getCorrectAnswer());
        dto.setExplanation(task.getExplanation());

        return dto;
    }

    private static List<String> parseOptions(String optionsJson) {
        try {
            return objectMapper.readValue(
                    optionsJson,
                    new TypeReference<List<String>>() {}
            );
        } catch (Exception e) {
            return List.of();
        }
    }
}
