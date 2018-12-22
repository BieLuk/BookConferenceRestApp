package bepoland.bookconference.dto;

import lombok.Data;

@Data
public class RoomDTO {

    private Long id;
    private String name;
    private String locationDescription;
    private Integer numberOfSeats;
    private Boolean hasProjector = false;
    private String phone;
}
