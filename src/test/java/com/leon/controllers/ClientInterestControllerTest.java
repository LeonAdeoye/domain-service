package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.ClientInterest;
import com.leon.models.Side;
import com.leon.services.ClientInterestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientInterestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientInterestService clientInterestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/interest/reconfigure"))
                .andExpect(status().isNoContent());
        verify(clientInterestService, times(1)).reconfigure();
    }

    @Test
    public void getAll_withValidOwnerId_shouldReturnListOfClientInterests() throws Exception {
        String ownerId = "testOwner";
        List<ClientInterest> interests = new ArrayList<>();
        ClientInterest interest1 = new ClientInterest();
        interest1.setClientInterestId(UUID.randomUUID());
        interest1.setClientId(UUID.randomUUID());
        interest1.setNotes("Test Notes 1");
        interest1.setSide(Side.BUY);
        interest1.setInstrumentCode("AAPL");
        interest1.setOwnerId(ownerId);
        ClientInterest interest2 = new ClientInterest();
        interest2.setClientInterestId(UUID.randomUUID());
        interest2.setClientId(UUID.randomUUID());
        interest2.setNotes("Test Notes 2");
        interest2.setSide(Side.SELL);
        interest2.setInstrumentCode("GOOGL");
        interest2.setOwnerId(ownerId);
        interests.add(interest1);
        interests.add(interest2);

        when(clientInterestService.getAll(ownerId)).thenReturn(interests);

        mockMvc.perform(get("/interest")
                .param("ownerId", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].notes").value("Test Notes 1"))
                .andExpect(jsonPath("$[1].notes").value("Test Notes 2"));
    }

    @Test
    public void getAll_withEmptyOwnerId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/interest")
                .param("ownerId", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_withValidData_shouldSaveClientInterest() throws Exception {
        ClientInterest interest = new ClientInterest();
        interest.setClientInterestId(UUID.randomUUID());
        interest.setClientId(UUID.randomUUID());
        interest.setNotes("New Interest");
        interest.setSide(Side.BUY);
        interest.setInstrumentCode("MSFT");
        interest.setOwnerId("testOwner");
        when(clientInterestService.save(any(ClientInterest.class))).thenReturn(interest);

        mockMvc.perform(post("/interest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("New Interest"));
    }

    @Test
    public void update_withValidData_shouldUpdateClientInterest() throws Exception {
        ClientInterest interest = new ClientInterest();
        interest.setClientInterestId(UUID.randomUUID());
        interest.setClientId(UUID.randomUUID());
        interest.setNotes("Updated Interest");
        interest.setSide(Side.SELL);
        interest.setInstrumentCode("AMZN");
        interest.setOwnerId("testOwner");
        when(clientInterestService.update(any(ClientInterest.class))).thenReturn(interest);

        mockMvc.perform(put("/interest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Updated Interest"));
    }

    @Test
    public void update_withNullClientInterest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/interest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_withValidIds_shouldDeleteClientInterest() throws Exception {
        String ownerId = "testOwner";
        String interestId = UUID.randomUUID().toString();
        doNothing().when(clientInterestService).delete(ownerId, interestId);

        mockMvc.perform(delete("/interest")
                .param("ownerId", ownerId)
                .param("clientInterestId", interestId))
                .andExpect(status().isNoContent());
        verify(clientInterestService, times(1)).delete(ownerId, interestId);
    }

    @Test
    public void delete_withEmptyOwnerId_shouldReturnBadRequest() throws Exception {
        String interestId = UUID.randomUUID().toString();
        mockMvc.perform(delete("/interest")
                .param("ownerId", "")
                .param("clientInterestId", interestId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_withEmptyInterestId_shouldReturnBadRequest() throws Exception {
        String ownerId = "testOwner";
        mockMvc.perform(delete("/interest")
                .param("ownerId", ownerId)
                .param("clientInterestId", ""))
                .andExpect(status().isBadRequest());
    }
} 