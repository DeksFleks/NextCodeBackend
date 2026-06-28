package dbataev.nextcode.client;

import dbataev.nextcode.model.dto.judge0.Judge0SubmissionRequest;
import dbataev.nextcode.model.dto.judge0.Judge0SubmissionResponse;
import dbataev.nextcode.model.dto.judge0.LanguageDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class Judge0Client {
    private final RestClient restClient;

    public Judge0Client(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<LanguageDto> getLanguages() {
        return restClient.get()
                .uri("/languages")
                .retrieve()
                .body(new ParameterizedTypeReference<List<LanguageDto>>() {});
    }

    public Judge0SubmissionResponse execute(Judge0SubmissionRequest requestDto) {

        System.out.println("language = " + requestDto.getLanguageId());
        System.out.println("code = " + requestDto.getSourceCode());
        System.out.println("stdin = " + requestDto.getStdin());

        return restClient.post()
                .uri("submissions?wait=true")
                .body(requestDto)
                .retrieve()
                .body(Judge0SubmissionResponse.class);
    }
}
