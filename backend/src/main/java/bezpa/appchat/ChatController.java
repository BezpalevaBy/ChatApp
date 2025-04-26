package bezpa.appchat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User existing = userRepo.findByEmail(user.getEmail());
        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            return existing;
        } else {
            throw new RuntimeException("Пользователя нет");
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

    @GetMapping("/users/{email}")
public List<User> getUsers(@PathVariable String email) {
    Set<String> emails = new HashSet<>();

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



    @PostMapping("/messages")
    public Message sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepo.save(message);
    }

    @GetMapping("/messages/{email}")
    public List<Message> getMessages(@PathVariable String email) {
        return messageRepo.findBySenderOrRecipientOrderByTimestampAsc(email, email);
    }

    @GetMapping("/messages/{email}/{targetEmail}")
    public List<Message> getMessagesBetweenUsers(@PathVariable String email, @PathVariable String targetEmail) {
        logger.info("Fetching messages between {} and {}", email, targetEmail);

        List<Message> messages = messageRepo.findBySenderOrRecipientOrderByTimestampAsc(email, email);

        return messages.stream()
                .filter(m -> (m.getSender().equals(email) && m.getRecipient().equals(targetEmail)) ||
                             (m.getSender().equals(targetEmail) && m.getRecipient().equals(email)))
                .collect(Collectors.toList());
    }
}
