package bezpa.appchat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrRecipientOrderByTimestampAsc(String sender, String recipient);
}