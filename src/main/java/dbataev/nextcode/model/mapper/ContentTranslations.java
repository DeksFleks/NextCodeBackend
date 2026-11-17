package dbataev.nextcode.model.mapper;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
public final class ContentTranslations {
    private ContentTranslations() {}
    public static <T> T resolve(List<T> rows, Function<T, Long> language, Long requested) {
        return rows.stream().filter(t -> requested.equals(language.apply(t))).findFirst()
            .or(() -> rows.stream().filter(t -> Long.valueOf(1).equals(language.apply(t))).findFirst())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Missing content translation"));
    }
}
