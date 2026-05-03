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
}
