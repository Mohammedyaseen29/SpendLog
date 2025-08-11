package authservices.entities;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")

public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
   private Long id;
    @Column(nullable = false,unique = true)
   private String username;
    @Column(nullable = false)
   private String password;

    @Enumerated(EnumType.STRING)

    private Role role;




}
