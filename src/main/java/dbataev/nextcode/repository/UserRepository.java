package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from users u where u.id = :id")
    Optional<User> findForUpdate(@org.springframework.data.repository.query.Param("id") Long id);

    User findByUsername(String username);

    @Query(value = "SELECT nextval('developer_nickname_seq')", nativeQuery = true)
    Long getNextDeveloperNicknameNumber();
}
