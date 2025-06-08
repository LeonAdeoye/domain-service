package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Client;
import com.leon.services.ClientService;
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
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/client/reconfigure"))
                .andExpect(status().isNoContent());
        verify(clientService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        Client client1 = new Client();
        client1.setClientId(UUID.randomUUID());
        client1.setClientName("Client 1");
        Client client2 = new Client();
        client2.setClientId(UUID.randomUUID());
        client2.setClientName("Client 2");
        clients.add(client1);
        clients.add(client2);

        when(clientService.getAll()).thenReturn(clients);

        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].clientName").value("Client 1"))
                .andExpect(jsonPath("$[1].clientName").value("Client 2"));
    }

    @Test
    public void save_withValidData_shouldSaveClient() throws Exception {
        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        client.setClientName("New Client");
        when(clientService.save(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("New Client"));
    }

    @Test
    public void save_withNullClient_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void save_withEmptyName_shouldReturnBadRequest() throws Exception {
        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        client.setClientName("");

        mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_withValidId_shouldDeleteClient() throws Exception {
        String clientId = UUID.randomUUID().toString();
        doNothing().when(clientService).delete(clientId);

        mockMvc.perform(delete("/client")
                .param("clientId", clientId))
                .andExpect(status().isNoContent());
        verify(clientService, times(1)).delete(clientId);
    }

    @Test
    public void delete_withEmptyId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/client")
                .param("clientId", ""))
                .andExpect(status().isBadRequest());
    }
} 