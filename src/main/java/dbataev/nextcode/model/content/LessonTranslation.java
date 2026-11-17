package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lesson_translations")
public class LessonTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_translation_id")
    private Long id;
    @Column(name = "language_id")
    private Long languageId;
    @Column(name = "title")
    private String title;
    @Column(name = "theory_text")
    private String theoryText;
}
