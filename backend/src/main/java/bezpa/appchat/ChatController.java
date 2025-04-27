package bezpa.appchat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existing = userRepo.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (existing.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> existing = userRepo.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    // Получить всех пользователей (если хочешь всех)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Получить сообщения между пользователями
    @GetMapping("/messages/{email}/{targetEmail}")
    public List<Message> getMessagesBetweenUsers(@PathVariable String email, @PathVariable String targetEmail) {
        logger.info("Fetching messages between {} and {}", email, targetEmail);

        List<Message> messages = messageRepo.findBySenderOrRecipientOrderByTimestampAsc(email, email);

        return messages.stream()
                .filter(m -> (m.getSender().equals(email) && m.getRecipient().equals(targetEmail)) ||
                             (m.getSender().equals(targetEmail) && m.getRecipient().equals(email)))
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{email}")
    public List<User> getUsers(@PathVariable String email) {
    HashSet<String> emails = new HashSet<>();

    var messages = messageRepo.findBySenderOrRecipientOrderByTimestampAsc(email, email);
    for (Message message : messages) {
        emails.add(message.getSender());
        emails.add(message.getRecipient());
    }

    emails.remove(email);

    List<User> users = userRepo.findAllByEmailIn(emails);

    logger.info(users.toString());

    return users;
    }

    // Отправить сообщение
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepo.save(message);
        return ResponseEntity.ok().build();
    }
}
