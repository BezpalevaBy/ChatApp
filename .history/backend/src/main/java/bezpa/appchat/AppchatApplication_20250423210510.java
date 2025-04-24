package bezpa.appchat;

import org.apache.commons.math3.analysis.function.Identity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppchatApplication.class, args);
	}

}

Identity
public class User {
  @Id @GeneratedValue
  private Long id;
  private String username;
  private String password;
}

