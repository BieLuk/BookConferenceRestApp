package bepoland.bookconference.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@DynamicUpdate
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotBlank
    private String name;

    @Size(max = 100)
    @NotBlank
    private String surname;

    @Size(max = 100)
    @NotBlank
    @Column(unique = true)
    private String login;

    @Size(min = 6, max = 100)
    @NotBlank
    private String password;

    private Boolean available = true;

}
