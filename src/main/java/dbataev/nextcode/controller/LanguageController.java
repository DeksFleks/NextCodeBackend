package dbataev.nextcode.controller;
import dbataev.nextcode.model.content.Language;
import dbataev.nextcode.service.ContentLanguageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api")
public class LanguageController {
    private final ContentLanguageService languages;
    public LanguageController(ContentLanguageService languages) { this.languages = languages; }
    @GetMapping("/languages")
    public List<Language> list() { return languages.active(); }
    @PutMapping("/users/language/{languageId}")
    public void select(Authentication auth, @PathVariable Long languageId) { languages.select((Long)auth.getPrincipal(), languageId); }
}
