package commerce;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Shopper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateKey;

    @Column(unique = true)
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(length = 1000)
    private String hashedPassword;

}
