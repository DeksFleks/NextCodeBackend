package dbataev.nextcode.model.dto.compiler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilerResponse {
    private String output;
    private String error;
    private String compileError;
    private boolean success;
}

