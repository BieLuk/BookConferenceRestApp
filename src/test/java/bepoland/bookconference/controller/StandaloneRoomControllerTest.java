package bepoland.bookconference.controller;

import bepoland.bookconference.dto.RoomCreateDTO;
import bepoland.bookconference.dto.RoomDTO;
import bepoland.bookconference.dto.RoomEditDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sun.plugin2.util.PojoUtil.toJson;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(RoomController.class)
@AutoConfigureRestDocs
public class StandaloneRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Value("${app.adminPass}")
    private String adminPass;


    @Test
    public void givenRooms_whenGetAvailableRooms_thenReturnJsonArray() throws Exception {

        RoomDTO room1 = new RoomDTO("Small room", "1st floor", 8, false, "+48-111-222-333");
        RoomDTO room2 = new RoomDTO("Medium room", "2nd floor", 14, true, "+48-999-888-777");

        List<RoomDTO> allRooms = Arrays.asList(room1, room2);

        given(roomService.getAvailableRooms()).willReturn(allRooms);

        mockMvc.perform(get("/room/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(document("available-room-success"));
    }


    @Test
    public void givenRoomDto_whenCreateRoom_thenReturnApiResponse_success() throws Exception {

        RoomCreateDTO room = new RoomCreateDTO("Small room", "1st floor", 8, false, "123456789");

        given(roomService.createRoom(room, adminPass)).willReturn(new ApiResponse(true, "Room created successfully."));

        mockMvc.perform(post("/room/create").header("x-api-key", adminPass)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room created successfully."))
                .andDo(document("create-room-success"));
    }


    @Test
    public void givenRoomDto_whenCreateRoom_thenReturnApiResponse_wrongPass() throws Exception {

        RoomCreateDTO room = new RoomCreateDTO("Small room", "1st floor", 8, false, "123456789");

        given(roomService.createRoom(room, "qwe123")).willReturn(new ApiResponse(false, "Wrong password key."));

        mockMvc.perform(post("/room/create").header("x-api-key", "qwe123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Wrong password key."));
    }

    @Test
    public void givenRoomDto_whenEditRoom_thenReturnApiResponse_success() throws Exception {

        RoomEditDTO room = new RoomEditDTO("Small room", "2nd floor", 10, true, "123456789");

        given(roomService.editRoom(room, adminPass)).willReturn(new ApiResponse(true, "Room edited successfully."));

        mockMvc.perform(put("/room/edit").header("x-api-key", adminPass)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room edited successfully."))
                .andDo(document("edit-room-success"));
    }

    @Test
    public void givenRoomName_whenDeleteRoom_thenReturnApiResponse_success() throws Exception {

        String roomName = "Medium room";

        given(roomService.deleteRoom(roomName, adminPass)).willReturn(new ApiResponse(true, "Room deleted successfully."));

        mockMvc.perform(delete("/room/delete").header("x-api-key", adminPass)
                .param("roomName", roomName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Room deleted successfully."))
                .andDo(document("delete-room-success"));
    }

    @Test
    public void givenRoomName_whenDeleteRoom_thenReturnApiResponse_badRoomName() throws Exception {

        String badRoomName = "abc";

        given(roomService.deleteRoom(badRoomName, adminPass)).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(delete("/room/delete").header("x-api-key", adminPass)
                .param("roomName", badRoomName))
                .andExpect(status().is4xxClientError());
    }

}
