package bepoland.bookconference.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RoomEditDTO {

    @Size(max = 50)
    @NotBlank
    @Column(unique = true)
    private String name;

    @Size(max = 256)
    private String locationDescription;

    @Max(100)
    private Integer numberOfSeats;

    private Boolean hasProjector = false;

    @Size(max = 100)
    private String phone;
}
