package commerce;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(length = 1000)
    private String hashedPassword;
}
