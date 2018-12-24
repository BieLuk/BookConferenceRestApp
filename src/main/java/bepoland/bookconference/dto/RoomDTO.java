package bepoland.bookconference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomDTO {

    private String name;
    private String locationDescription;
    private Integer numberOfSeats;
    private Boolean hasProjector;
    private String phone;
}
