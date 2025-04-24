package bezpa.appchat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
