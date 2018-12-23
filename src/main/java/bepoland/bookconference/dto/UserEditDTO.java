package bepoland.bookconference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEditDTO {

    @Size(max = 50)
    private String name;

    @Size(max = 100)
    private String surname;

    @Size(max = 100)
    @Column(unique = true)
    @NotBlank
    private String login;

    @Size(min = 6, max = 100)
    private String password;
}
