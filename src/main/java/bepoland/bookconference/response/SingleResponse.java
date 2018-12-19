package bepoland.bookconference.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SingleResponse<T> extends Response {
    private T data;

}
