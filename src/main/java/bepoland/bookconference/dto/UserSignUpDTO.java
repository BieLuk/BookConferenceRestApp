package bepoland.bookconference.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserSignUpDTO {

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