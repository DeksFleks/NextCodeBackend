package dbataev.nextcode.model.dto;
import jakarta.validation.constraints.*;
import java.util.List;
public record TaskAnswer(@NotNull @Positive Long lessonId,
    @Size(max = 100) List<@NotNull @Positive Long> optionIds,
    @Size(max = 100000) String answer) {}
