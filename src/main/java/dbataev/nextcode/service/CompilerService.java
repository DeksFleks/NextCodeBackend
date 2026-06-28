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
        Integer language = 62;

        Judge0SubmissionResponse response  = judge0Client.execute(
            new Judge0SubmissionRequest(
                    language,
                    compilerRequest.getCode(),
                    compilerRequest.getInput()
            )
        );

        return new CompilerResponse(
                response.getStdout(),
                response.getStderr(),
                response.getCompileOutput(),
                response.getStatus().getId() == 3
        );
    }
}
