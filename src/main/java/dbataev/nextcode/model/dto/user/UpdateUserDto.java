package dbataev.nextcode.model.dto.user;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String username;
    private String nickname;
    private String password;
}
