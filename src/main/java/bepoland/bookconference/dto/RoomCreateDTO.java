package bepoland.bookconference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomCreateDTO {

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
}
