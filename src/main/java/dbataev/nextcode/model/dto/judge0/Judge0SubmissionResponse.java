package dbataev.nextcode.model.dto.judge0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Judge0SubmissionResponse {
    private String stdout;
    private String stderr;

    @JsonProperty("compile_output")
    private String compileOutput;

    private Judge0Status status;
}
