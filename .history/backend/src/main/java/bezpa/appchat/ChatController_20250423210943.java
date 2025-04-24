package bezpa.appchat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {}

@RestController
@RequestMapping("/api")
public class ChatController {

  @Autowired
  private MessageRepository repo;

  @GetMapping("/messages")
  public List<Message> getMessages() {
    return repo.findAll();
  }

  @PostMapping("/messages")
  public Message sendMessage(@RequestBody Message message) {
    message.setTimestamp(LocalDateTime.now());
    return repo.save(message);
  }
}
