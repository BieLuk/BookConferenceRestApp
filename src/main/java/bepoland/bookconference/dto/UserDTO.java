package bepoland.bookconference.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {

    private String name;
    private String surname;
    private String login;
}
