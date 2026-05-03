package dbataev.nextcode.model.dto.user;

import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String password;
    private String nickname;
}
