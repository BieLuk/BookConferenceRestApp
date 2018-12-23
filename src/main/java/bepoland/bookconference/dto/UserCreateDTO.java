package bepoland.bookconference.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {

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
}