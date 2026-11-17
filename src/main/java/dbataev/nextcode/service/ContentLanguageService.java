package dbataev.nextcode.service;
import dbataev.nextcode.repository.*;
import dbataev.nextcode.model.content.Language;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class ContentLanguageService {
    private final UserRepository users;
    private final LanguageRepository languages;
    public ContentLanguageService(UserRepository users, LanguageRepository languages) {
        this.users = users; this.languages = languages;
    }
    public Long forUser(Long userId) {
        return users.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getLanguageId();
    }
    public List<Language> active() { return languages.findByIsActiveTrueOrderByIdAsc(); }
    @org.springframework.transaction.annotation.Transactional
    public void select(Long userId, Long languageId) {
        Language language = languages.findById(languageId)
            .filter(l -> Boolean.TRUE.equals(l.getIsActive()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language is unavailable"));
        var user = users.findForUpdate(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setLanguageId(language.getId()); users.save(user);
    }
}
