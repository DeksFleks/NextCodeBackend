package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT nextval('developer_nickname_seq')", nativeQuery = true)
    Long getNextDeveloperNicknameNumber();
}
