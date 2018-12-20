package bepoland.bookconference.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private Boolean available;
}
