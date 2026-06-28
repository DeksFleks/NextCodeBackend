package dbataev.nextcode.model.dto.compiler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilerRequest {
    private String code;
    private String input;
    private String language;
}
