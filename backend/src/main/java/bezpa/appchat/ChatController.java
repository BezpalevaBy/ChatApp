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

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        Optional<User> existing = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
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

    logger.info("Users connected with {}: {}", email, users);

    return users;
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        Optional<User> recipientUser = userRepository.findByEmail(message.getRecipient());
        if (recipientUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipient does not exist");
        }
        message.setTimestamp(LocalDateTime.now());
        messageRepo.save(message);
        logger.info("Message sent from {} to {}", message.getSender(), message.getRecipient());
        return ResponseEntity.ok().build();
    }
}
