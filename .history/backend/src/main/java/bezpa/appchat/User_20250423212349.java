package bezpa.appchat;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
public class User {
@Id @GeneratedValue
  private Long id;
  private String username;
  private String password;
}
