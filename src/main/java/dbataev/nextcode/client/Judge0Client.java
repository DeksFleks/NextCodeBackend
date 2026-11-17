package dbataev.nextcode.client;

import dbataev.nextcode.model.dto.judge0.Judge0SubmissionRequest;
import dbataev.nextcode.model.dto.judge0.Judge0SubmissionResponse;
import dbataev.nextcode.model.dto.judge0.LanguageDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, Object> body = new HashMap<>();

        body.put("language_id", 62);
        body.put("source_code", requestDto.getSourceCode());
        body.put("stdin", requestDto.getStdin());

        System.out.println(body);

        return restClient.post()
                .uri("/submissions?wait=true")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Judge0SubmissionResponse.class);
    }
}
