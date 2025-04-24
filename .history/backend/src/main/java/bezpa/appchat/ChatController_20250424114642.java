package bezpa.appchat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired private MessageRepository messageRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User existing = userRepo.findByEmail(user.getEmail());
        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            return existing;
        } else {
            throw new RuntimeException("Неверные учетные данные");
        }
    }

    @PostMapping("/messages")
    public Message sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @GetMapping("/messages/{email}")
    public List<Message> getMessages(@PathVariable String email) {
        return messageRepo.findBySenderOrRecipientOrderByTimestampAsc(email, email);
    }
}
