package bezpa.appchat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        User existing = userRepo.findByEmail(user.getEmail());
        if (existing != null) {
            throw new RuntimeException("Пользователь уже существует");
        }
        return userRepo.save(user);
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
