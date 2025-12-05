package commerce;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "sellerId"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataKey;

    @Column(unique = true)
    private UUID id;

    private UUID sellerId;

    private String name;

    private String imageUri;

    private String description;

    private BigDecimal priceAmount;

    private Integer stockQuantity;

    private LocalDateTime registeredTimeUtc;
}
