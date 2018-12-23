package bepoland.bookconference.dto;

import lombok.Data;

@Data
public class RoomDTO {

    private String name;
    private String locationDescription;
    private Integer numberOfSeats;
    private Boolean hasProjector;
    private String phone;
}
