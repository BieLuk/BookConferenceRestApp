package bepoland.bookconference.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotBlank
    @Column(unique = true)
    private String name;

    @Size(max = 256)
    private String locationDescription;

    @Max(100)
    @NotNull
    private Integer numberOfSeats;

    private Boolean hasProjector = false;

    @Size(max = 100)
    private String phone;

    private Boolean available = true;


}
