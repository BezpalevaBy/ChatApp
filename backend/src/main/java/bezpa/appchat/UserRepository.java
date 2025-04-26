package bezpa.appchat;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAllByEmailIn(Set<String> emails);
}
