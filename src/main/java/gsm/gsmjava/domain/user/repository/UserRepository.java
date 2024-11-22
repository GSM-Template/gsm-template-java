package gsm.gsmjava.domain.user.repository;

import gsm.gsmjava.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
