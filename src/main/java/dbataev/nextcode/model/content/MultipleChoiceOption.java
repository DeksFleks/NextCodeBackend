package dbataev.nextcode.model.content;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "multiple_choice_options")
public class MultipleChoiceOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @Column(name = "order_index")
    private Integer orderIndex;
    @OneToMany
    @JoinColumn(name = "option_id")
    private List<OptionTranslation> translations;
}
