package dbataev.nextcode.service;

import dbataev.nextcode.LessonState;
import dbataev.nextcode.model.base.Lesson;
import dbataev.nextcode.repository.UserProgressRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {
    private final UserProgressRepository userProgressRepository;

    public LessonService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }


}
