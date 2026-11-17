package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "option_translations")
public class OptionTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_id")
    private Long id;
    @Column(name = "language_id")
    private Long languageId;
    @Column(name = "option_text")
    private String optionText;
}
