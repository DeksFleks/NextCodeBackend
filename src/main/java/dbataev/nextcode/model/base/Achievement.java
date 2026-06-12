package dbataev.nextcode.model.base;

import dbataev.nextcode.enums.AchievementType;
import dbataev.nextcode.enums.LevelAchievementType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    @Column(name = "condition_value")
    private Integer conditionValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "level_type")
    @Enumerated(EnumType.STRING)
    private LevelAchievementType levelType;
}
