//package com.yolo.chef;
//
//import com.yolo.chef.dto.CreateUserProfileRequest;
//import com.yolo.chef.user.User;
//import com.yolo.chef.user.UserRepository;
//import com.yolo.chef.address.AddressRepository;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ActiveProfiles("test")
//public class UserProfileControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Order(1)
//    @Test
//    public void testCreateUserProfileSuccess() throws Exception {
//        // Given: Create a user in the database
//        User user = new User();
//        user.setUsername("john_doe");
//        user.setEmail("john@example.com");
//        user.setPassword("password");
//        userRepository.save(user);
//
//        // Given: Create a valid user profile request
//        CreateUserProfileRequest userProfileRq = new CreateUserProfileRequest();
//        userProfileRq.setFirstName("John");
//        userProfileRq.setLastName("Doe");
//        userProfileRq.setContactNumber("1234567890");
//        userProfileRq.setHouse("123");
//        userProfileRq.setStreet("Main St");
//        userProfileRq.setArea("Downtown");
//        userProfileRq.setZipCode("12345");
//        userProfileRq.setCity("Cityville");
//        userProfileRq.setCountry("Countryland");
//        userProfileRq.setCurrencyCode("USD");
//
//        String requestContent = "{\n" +
//                "\"firstName\":\"John\",\n" +
//                "\"lastName\":\"Doe\",\n" +
//                "\"contactNumber\":\"1234567890\",\n" +
//                "\"house\":\"123\",\n" +
//                "\"street\":\"Main St\",\n" +
//                "\"area\":\"Downtown\",\n" +
//                "\"zipCode\":\"12345\",\n" +
//                "\"city\":\"Cityville\",\n" +
//                "\"country\":\"Countryland\",\n" +
//                "\"currencyCode\":\"USD\"\n" +
//                "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/john_doe/userProfiles")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(requestContent))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("User profile created successfully")));
//    }
//
//    @Order(2)
//    @Test
//    public void testCreateUserProfileUserNotFound() throws Exception {
//        // Given: Create a valid user profile request
//        CreateUserProfileRequest userProfileRq = new CreateUserProfileRequest();
//        userProfileRq.setFirstName("Jane");
//        userProfileRq.setLastName("Doe");
//        userProfileRq.setContactNumber("0987654321");
//        userProfileRq.setHouse("456");
//        userProfileRq.setStreet("Second St");
//        userProfileRq.setArea("Uptown");
//        userProfileRq.setZipCode("54321");
//        userProfileRq.setCity("Townsville");
//        userProfileRq.setCountry("Countryland");
//        userProfileRq.setCurrencyCode("EUR");
//
//        String requestContent = "{\n" +
//                "\"firstName\":\"Jane\",\n" +
//                "\"lastName\":\"Doe\",\n" +
//                "\"contactNumber\":\"0987654321\",\n" +
//                "\"house\":\"456\",\n" +
//                "\"street\":\"Second St\",\n" +
//                "\"area\":\"Uptown\",\n" +
//                "\"zipCode\":\"54321\",\n" +
//                "\"city\":\"Townsville\",\n" +
//                "\"country\":\"Countryland\",\n" +
//                "\"currencyCode\":\"EUR\"\n" +
//                "}";
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/non_existent_user/userProfiles")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(requestContent))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("User not found")));
//    }
//
//    @Order(3)
//    @Test
//    public void testCreateUserProfileInternalServerError() throws Exception {
//        // Simulate a server error, for instance by having a bad configuration
//
//        // Given: Create a valid user profile request
//        CreateUserProfileRequest userProfileRq = new CreateUserProfileRequest();
//        userProfileRq.setFirstName("Alice");
//        userProfileRq.setLastName("Smith");
//        userProfileRq.setContactNumber("1231231234");
//        userProfileRq.setHouse("789");
//        userProfileRq.setStreet("Third St");
//        userProfileRq.setArea("Midtown");
//        userProfileRq.setZipCode("67890");
//        userProfileRq.setCity("Cityburg");
//        userProfileRq.setCountry("Countryland");
//        userProfileRq.setCurrencyCode("GBP");
//
//        String requestContent = "{\n" +
//                "\"firstName\":\"Alice\",\n" +
//                "\"lastName\":\"Smith\",\n" +
//                "\"contactNumber\":\"1231231234\",\n" +
//                "\"house\":\"789\",\n" +
//                "\"street\":\"Third St\",\n" +
//                "\"area\":\"Midtown\",\n" +
//                "\"zipCode\":\"67890\",\n" +
//                "\"city\":\"Cityburg\",\n" +
//                "\"country\":\"Countryland\",\n" +
//                "\"currencyCode\":\"GBP\"\n" +
//                "}";
//
//        // Here, you would need to configure your service to simulate an internal server error
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/john_doe/userProfiles")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(requestContent))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("An error occurred while creating the user profile")));
//    }
//}
