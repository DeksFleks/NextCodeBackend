package dbataev.nextcode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Judge0Config {

    @Bean
    public RestClient judge0WebClient(@Value("${judge0.url}") String url){
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }
}