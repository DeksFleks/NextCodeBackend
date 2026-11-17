package dbataev.nextcode.service;

import dbataev.nextcode.client.Judge0Client;
import dbataev.nextcode.model.dto.compiler.CompilerRequest;
import dbataev.nextcode.model.dto.compiler.CompilerResponse;
import dbataev.nextcode.model.dto.judge0.Judge0SubmissionRequest;
import dbataev.nextcode.model.dto.judge0.Judge0SubmissionResponse;
import dbataev.nextcode.model.dto.judge0.LanguageDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompilerService {
    private final Judge0Client judge0Client;

    public CompilerService(Judge0Client judge0Client) {
        this.judge0Client = judge0Client;
    }

    public List<LanguageDto> getLanguages() {
        return judge0Client.getLanguages();
    }

    public CompilerResponse execute(CompilerRequest compilerRequest) {

        Integer languageId = switch (compilerRequest.getLanguage().toLowerCase()) {
            case "java" -> 62;
            case "c#" -> 51;
            default -> throw new IllegalArgumentException(
                    "Unsupported language: " + compilerRequest.getLanguage()
            );
        };

        Judge0SubmissionResponse response = judge0Client.execute(
                new Judge0SubmissionRequest(
                        languageId,
                        compilerRequest.getCode(),
                        compilerRequest.getInput()
                )
        );

        return new CompilerResponse(
                response.getStdout(),
                response.getStderr(),
                response.getCompileOutput(),
                response.getStatus() != null && response.getStatus().getId() == 3
        );
    }
}
