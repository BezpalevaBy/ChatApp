package bezpa.appchat;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
public class Message {
  @Id @GeneratedValue
  private Long id;
  private String sender;
  private String content;
  public LocalDateTime timestamp;

  public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getSender() {
    return sender;
}

public void setSender(String sender) {
    this.sender = sender;
}

public String getContent() {
    return content;
}

public void setContent(String content) {
    this.content = content;
}

public LocalDateTime getTimestamp() {
    return timestamp;
}

public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
}
}