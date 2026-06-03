package dbataev.nextcode.model.base;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private Integer  xp;
    private Integer level;
    private Integer  streak;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "xp_for_next_level")
    private Integer xpForNextLevel;

    @ManyToOne
    @JoinColumn(name = "current_course_id")
    private Course currentCourse;

    @Column(name = "total_xp", nullable = false)
    private Long totalXp = 0L;

    @Column(name = "best_streak", nullable = false)
    private Long bestStreak;

}
