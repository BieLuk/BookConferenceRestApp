package bepoland.bookconference.controller;

import bepoland.bookconference.dto.UserCreateDTO;
import bepoland.bookconference.dto.UserDTO;
import bepoland.bookconference.dto.UserEditDTO;
import bepoland.bookconference.response.ApiResponse;
import bepoland.bookconference.service.UserService;
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
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
public class StandaloneUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Value("${app.adminPass}")
    private String adminPass;

    @Test
    public void givenUsers_whenGetAvailableUsers_thenReturnJsonArray() throws Exception {

        UserDTO user1 = new UserDTO("John", "Smith", "jsmith");
        UserDTO user2 = new UserDTO("Jane", "Doe", "jdoe");

        List<UserDTO> allUsers = Arrays.asList(user1, user2);

        given(userService.getAvailableUsers()).willReturn(allUsers);

        mockMvc.perform(get("/user/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(document("available-user-success"));

    }


    @Test
    public void givenUserDto_whenCreateUser_thenReturnApiResponse_success() throws Exception {

        UserCreateDTO user = new UserCreateDTO("Mark", "Bloom", "mbloom", "qazwsx");

        given(userService.createUser(user, adminPass)).willReturn(new ApiResponse(true, "User created successfully."));

        mockMvc.perform(post("/user/create").header("x-api-key", adminPass)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully."))
                .andDo(document("create-user-success"));
    }

    @Test
    public void givenUserDto_whenCreateUser_thenReturnApiResponse_loginTaken() throws Exception {

        UserCreateDTO user = new UserCreateDTO("Mark", "Bloom", "mbloom", "qazwsx");

        given(userService.createUser(user, adminPass)).willReturn(new ApiResponse(false, "Login is already taken."));

        mockMvc.perform(post("/user/create").header("x-api-key", adminPass)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Login is already taken."));
    }

    @Test
    public void givenUserDto_whenCreateUser_thenReturnApiResponse_wrongPass() throws Exception {

        UserCreateDTO user = new UserCreateDTO("Mark", "Bloom", "mbloom", "qazwsx");

        given(userService.createUser(user, "qwe123")).willReturn(new ApiResponse(false, "Wrong password key."));

        mockMvc.perform(post("/user/create").header("x-api-key", "qwe123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Wrong password key."));

    }

    @Test
    public void givenUserDto_whenEditUser_thenReturnApiResponse_success() throws Exception {

        UserEditDTO user = new UserEditDTO("John", "Bloom", "mbloom", "123456");

        given(userService.editUser(user, adminPass)).willReturn(new ApiResponse(true, "User edited successfully."));

        mockMvc.perform(put("/user/edit").header("x-api-key", adminPass)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User edited successfully."))
                .andDo(document("edit-user-success"));
    }

    @Test
    public void givenUserLogin_whenDeleteUser_thenReturnApiResponse_success() throws Exception {

        String userLogin = "jsmith";

        given(userService.deleteUser(userLogin, adminPass)).willReturn(new ApiResponse(true, "User deleted successfully."));

        mockMvc.perform(delete("/user/delete").header("x-api-key", adminPass)
                .param("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully."))
                .andDo(document("delete-user-success"));
    }


    @Test
    public void givenUserLogin_whenDeleteUser_thenReturnApiResponse_badLogin() throws Exception {

        String badUserLogin = "abc";

        given(userService.deleteUser(badUserLogin, adminPass)).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(delete("/user/delete").header("x-api-key", adminPass)
                .param("userLogin", badUserLogin))
                .andExpect(status().is4xxClientError());
    }

}
