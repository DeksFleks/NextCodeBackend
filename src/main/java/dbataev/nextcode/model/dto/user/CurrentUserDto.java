package dbataev.nextcode.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUserDto {
    Long id;
    String username;
    String nickname;
    Integer xp;
    Integer level;
    Integer streak;
}
